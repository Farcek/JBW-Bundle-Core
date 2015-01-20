/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mn.le.farcek.jbw.bundle.core.text;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import mn.le.farcek.common.entity.FCoreEntity;
import mn.le.farcek.jbw.bundle.beanForm.Form;
import mn.le.farcek.jbw.bundle.beanView.DataTable;
import mn.le.farcek.validation.Email;
import mn.le.farcek.validation.NotEmpty;

/**
 *
 * @author Farcek
 */
@Entity
@Form(fields = {"key","text"})
@DataTable(pk = "id", fields = {"key","text"})
public class TextEntity extends FCoreEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TextEntity")
    private Integer id;

    
    @NotEmpty
    @Column(nullable = false, unique = true, name = "ky")
    private String key;
    
    
    @Column(columnDefinition = "text")
    private String text;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    
    
}
