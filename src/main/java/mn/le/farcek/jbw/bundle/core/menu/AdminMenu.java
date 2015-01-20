/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.core.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mn.le.farcek.common.entity.criteria.BinaryOperator;
import mn.le.farcek.common.entity.criteria.FCriteriaBuilder;
import mn.le.farcek.common.entity.ejb.FListResult;
import mn.le.farcek.common.utils.FCollectionUtils;
import mn.le.farcek.common.utils.FStringUtils;
import mn.le.farcek.jbw.api.Action;
import mn.le.farcek.jbw.api.Controller;
import mn.le.farcek.jbw.api.action.IActionRequest;
import mn.le.farcek.jbw.bundle.core.utils.SampleCrudController;
import mn.le.farcek.jbw.bundle.beanForm.el.SampleEntitySelectItem;



/**
 *
 * @author Farcek
 */
@Controller(name = "admin.menu")
public class AdminMenu extends SampleCrudController<MenuEntity> {

    public AdminMenu() {
        super(MenuEntity.class);
    }

    @Action(result = "json")
    public Collection parant(IActionRequest request) {

        FCriteriaBuilder<MenuEntity> builder = new FCriteriaBuilder<>(MenuEntity.class);

        String q = request.getParameter("q");
        if (FStringUtils.notEmpty(q)) {
            builder.add(new BinaryOperator("name", q + "%", "like"));
            builder.add(new BinaryOperator("keyName", q + "%", "like"));
        }

        FListResult<MenuEntity> entitysBy = service.entitysBy(builder);

        Collection<SampleEntitySelectItem> copy = FCollectionUtils.copy(entitysBy.getList(), new FCollectionUtils.Copy<MenuEntity, SampleEntitySelectItem>() {

            @Override
            public Collection<SampleEntitySelectItem> create() {
                return new ArrayList<>();
            }

            @Override
            public SampleEntitySelectItem item(MenuEntity s) {
                return new SampleEntitySelectItem(s.getId(), s.getName());
            }
        });

        return copy;
    }

}
