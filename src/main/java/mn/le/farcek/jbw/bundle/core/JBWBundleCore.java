/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.core;

import mn.le.farcek.jbw.bundle.core.text.AdminText;

import com.google.inject.Inject;

import java.util.List;

import mn.le.farcek.jbw.api.Bundle;

import mn.le.farcek.jbw.api.bundle.JBWBundle;
import mn.le.farcek.jbw.api.managers.IUrlBuilder;

import mn.le.farcek.jbw.bundle.admin.JBWAdminMenuManager;
import mn.le.farcek.jbw.bundle.core.menu.AdminMenu;

/**
 *
 * @author Farcek
 */
@Bundle(name = "core")
public class JBWBundleCore extends JBWBundle {

    @Override
    protected void setupControllers(List<Class<?>> controllers) {
        controllers.add(AdminText.class);
        controllers.add(AdminMenu.class);
    }

    

    @Inject
    void init(JBWAdminMenuManager adminMenuManager, IUrlBuilder urlBuilder) {
        
        
        adminMenuManager.add("core/menu", "core/admin.menu/index.html");
        adminMenuManager.add("core/text", "core/admin.text/index.html");
    }

}
