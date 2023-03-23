package jpql;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="CATEGORY")
public class Category {
	
	@Id
	@GeneratedValue
	@Column(name = "CATEGORY_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PARENT_ID")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	private List<Category> childList = new ArrayList<Category>();
	
	@ManyToMany
	@JoinTable(name="CATEGORY_ITEM",
	           joinColumns = @JoinColumn(name="CATEGORY_ID"),
	           inverseJoinColumns = @JoinColumn(name="ITEM_ID")) 
	private List<Item> items = new ArrayList<>();
	
	private String name;
	
	public void addChildCategory(Category child) {
		this.childList.add(child);
		child.setParent(this);
	}
	
	public void addItem(Item item) {
		this.items.add(item);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getChildList() {
		return childList;
	}

	public void setChildList(List<Category> childList) {
		this.childList = childList;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
