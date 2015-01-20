/*
 * Copyright (C) 2014 Farcek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mn.le.farcek.jbw.bundle.core.menu;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlRootElement;
import mn.le.farcek.beanform.Convertor;
import mn.le.farcek.common.entity.FCoreEntity;
import mn.le.farcek.common.objects.FActivedObject;
import mn.le.farcek.common.objects.FKeyedObject;
import mn.le.farcek.common.objects.FNamedObject;
import mn.le.farcek.common.objects.FSortedObject;
import mn.le.farcek.jbw.bundle.beanForm.DataSource;
import mn.le.farcek.jbw.bundle.beanForm.Form;
import mn.le.farcek.jbw.bundle.beanForm.entity.EntityConvertor;
import mn.le.farcek.jbw.bundle.beanView.DataTable;
import mn.le.farcek.validation.NotEmpty;

/**
 *
 * @author Farcek
 */
@Entity
@XmlRootElement
@Form(fields = {"name", "keyName", "sortIndex", "active", "parentEntity", "href", "target"})
@DataTable(pk = "id", fields = {"name", "keyName", "sortIndex", "active", "parentEntity", "href", "target"})
public class MenuEntity extends FCoreEntity implements FNamedObject, FSortedObject, FKeyedObject, FActivedObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MenuEntity")
    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String keyName;

    private Integer sortIndex;
    private Boolean active = true;

    private String href;
    private String target;

    @OneToOne
    @Convertor(EntityConvertor.class)
    @DataSource(bundleAction = "core/admin.menu/parant.json")
    private MenuEntity parentEntity;

    @OneToMany(mappedBy = "parentEntity", cascade = CascadeType.ALL)
    @OrderBy(value = "sortIndex ASC")
    private List<MenuEntity> childrenList;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MenuEntity getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(MenuEntity parentEntity) {
        this.parentEntity = parentEntity;
    }

    public List<MenuEntity> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<MenuEntity> childrenList) {
        this.childrenList = childrenList;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
