/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.core.utils;

import com.google.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;
import mn.le.farcek.beanform.BeanForm;
import mn.le.farcek.common.entity.FEntity;
import mn.le.farcek.common.entity.criteria.FCriteriaBuilder;
import mn.le.farcek.common.entity.ejb.FServiceException;
import mn.le.farcek.jbw.api.Action;
import mn.le.farcek.jbw.api.IBundle;
import mn.le.farcek.jbw.api.IConfig;
import mn.le.farcek.jbw.api.IService;
import mn.le.farcek.jbw.api.action.IActionMethod;
import mn.le.farcek.jbw.api.action.IActionRequest;
import mn.le.farcek.jbw.api.action.IActionSecurity;
import mn.le.farcek.jbw.api.action.IActionView;
import mn.le.farcek.jbw.api.action.view.HtmlView;
import mn.le.farcek.jbw.api.action.view.RedirectView;
import mn.le.farcek.jbw.api.bundle.BundleSetter;
import mn.le.farcek.jbw.api.exception.JBWPermissionsDenied;
import mn.le.farcek.jbw.api.exception.MissingBundle;
import mn.le.farcek.jbw.api.exception.MissingController;
import mn.le.farcek.jbw.api.managers.IUrlBuilder;
import mn.le.farcek.jbw.api.security.ISecurityManager;
import mn.le.farcek.jbw.api.utils.BundleUtil;
import mn.le.farcek.jbw.api.validation.ValidationManager;
import mn.le.farcek.jbw.bundle.beanForm.BeanFormFactory;
import mn.le.farcek.jbw.bundle.beanView.BeanViewFactory;
import mn.le.farcek.jbw.bundle.beanView.widgets.BVDataTable;

/**
 *
 * @author Farcek
 * @param <T>
 */
public abstract class SampleCrudController<T extends FEntity> implements BundleSetter {

    protected final Class<T> entityClass;

    @Inject
    protected BeanFormFactory formFactory;

    @Inject
    protected BeanViewFactory beanViewFactory;

    @Inject
    protected ValidationManager validationManager;

    @Inject
    protected IService service;

    @Inject
    protected IUrlBuilder urlBuilder;

    public SampleCrudController(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public String getRole() {
        return "admin";
    }

    private IBundle bundle;

    @Override
    public void setBundle(IBundle bundle) {
        this.bundle = bundle;
    }

    public IBundle getBundle() {
        return bundle;
    }

    protected BVDataTable factoryDataTable(IActionRequest request) {
        BVDataTable tblMember = beanViewFactory.factoryTable(entityClass);

        tblMember.setId(entityClass.getSimpleName());
        tblMember.setName(entityClass.getSimpleName());

        ActionUrl factoryActionUrl = factoryActionUrl();

        tblMember.setNewUrl(factoryActionUrl.build("new.html"));
        tblMember.setEditUrl(factoryActionUrl.build("edit.html"));
        tblMember.setDeleteUrl(factoryActionUrl.build("delete.html"));

        tblMember.loadParameters(request);

        FCriteriaBuilder<T> builder = new FCriteriaBuilder(entityClass);
        tblMember.bind(service, builder);

        return tblMember;
    }

    @Action
    public IActionView index(IActionRequest request, IActionSecurity security) {
        if (security.hasAccess(getRole()))
            throw new JBWPermissionsDenied(String.format("requared role `%s`", getRole()));

        BVDataTable dataTable = factoryDataTable(request);
        return new HtmlView(getIndexView()).add("dataTable", dataTable);
    }

    protected String getIndexView() {
        return "bundle://admin/sampleList";
    }

    protected String getFormView() {
        return "bundle://admin/sampleForm";
    }

    @Action
    public Object edit(IActionRequest request, IActionSecurity security) {
        if (security.hasAccess(getRole()))
            throw new JBWPermissionsDenied(String.format("requared role `%s`", getRole()));

        ActionUrl factoryActionUrl = factoryActionUrl();
        T entity = service.entityById(entityClass, request.getIntParam("_pkKey"));
        if (entity != null) {
            BeanForm<T> frm = getForm(entity);
            if (request.getMethod() == IActionMethod.Post) {
                frm.load(formFactory.factoryLoader(request));
                if (frm.valid())
                    try {
                        service.doUpdate(entityClass, entity);
                        return new RedirectView(factoryActionUrl.build("index.html") + "?update=true");
                    } catch (FServiceException ex) {
                        frm.addError(ex.getMessage());
                    }
            }

            frm.setAction(factoryActionUrl.build("edit.html") + "?_pkKey=" + entity.getId());
            return new HtmlView(getFormView()).add("frm", frm);
        }

        return new RedirectView(factoryActionUrl.build("index.html") + "?errormsg=no define entity");

    }

    @Action
    public Object delete(IActionRequest request, IActionSecurity security) {
        if (security.hasAccess(getRole()))
            throw new JBWPermissionsDenied(String.format("requared role `%s`", getRole()));

        ActionUrl factoryActionUrl = factoryActionUrl();
        T entity = service.entityById(entityClass, request.getIntParam("_pkKey"));
        if (entity != null)
            if (validationManager.valid(entity))
                try {
                    service.doDelete(entityClass, entity);
                    return new RedirectView(factoryActionUrl.build("index.html") + "?delete=true");
                } catch (FServiceException ex) {
                    return new RedirectView(factoryActionUrl.build("index.html") + "?errormsg=" + ex.getMessage());
                }
        return new RedirectView(factoryActionUrl.build("index.html") + "?errormsg=no define entity");
    }

    public BeanForm<T> getForm(T bean) {
        if (bean == null)
            bean = newInstance();
        BeanForm<T> frm = formFactory.factory(bean);
        return frm;
    }

    @Action(name = "new")
    public Object create(IActionRequest request, IActionSecurity security) {
        if (security.hasAccess(getRole()))
            throw new JBWPermissionsDenied(String.format("requared role `%s`", getRole()));

        BeanForm<T> frm = getForm(null);

        if (request.getMethod() == IActionMethod.Post) {
            frm.load(formFactory.factoryLoader(request));
            if (frm.valid())
                try {
                    service.doCreate(entityClass, frm.getBean());
                    return new RedirectView(factoryActionUrl().build("index.html") + "?add=true");
                } catch (FServiceException ex) {
                    frm.addError(ex.getMessage());
                }
        }

        return new HtmlView(getFormView()).add("frm", frm);
    }

    protected T newInstance() {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected ActionUrl factoryActionUrl() {
        ActionUrl ub = new ActionUrl();
        try {
            ub.setBundle(BundleUtil.getBundleName(getBundle().getClass()));
        } catch (MissingBundle ex) {

        }
        try {
            ub.setContoller(BundleUtil.getControllerName(getClass()));
        } catch (MissingController ex) {
        }
        return ub;
    }

    protected class ActionUrl {

        private String bundle;
        private String contoller;

        public String getBundle() {
            return bundle;
        }

        public void setBundle(String bundle) {
            this.bundle = bundle;
        }

        public String getContoller() {
            return contoller;
        }

        public void setContoller(String contoller) {
            this.contoller = contoller;
        }

        public String build(String action) {
            String url = String.format("%s/%s/%s", getBundle(), getContoller(), action);

            return urlBuilder.buildBundle(url, IUrlBuilder.Mode.SHORT);
        }

    }

}
