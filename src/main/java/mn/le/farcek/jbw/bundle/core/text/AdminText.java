/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.core.text;

import mn.le.farcek.jbw.api.Controller;
import mn.le.farcek.jbw.bundle.core.utils.SampleCrudController;



/**
 *
 * @author Farcek
 */
@Controller(name = "admin.text")
public class AdminText extends SampleCrudController<TextEntity> {

    public AdminText() {
        super(TextEntity.class);
    }
}
