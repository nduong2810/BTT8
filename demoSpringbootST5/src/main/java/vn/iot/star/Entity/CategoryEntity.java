package vn.iot.star.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id") // cột trong DB
	private Long categoryId;

	private String name;

	// Getter cho Thymeleaf: id
	public Long getId() {
		return categoryId; // ánh xạ categoryId thành id
	}

	// Setter & getter gốc
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}