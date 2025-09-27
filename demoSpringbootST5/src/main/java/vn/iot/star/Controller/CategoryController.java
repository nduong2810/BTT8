package vn.iot.star.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.iot.star.Entity.CategoryEntity;
import vn.iot.star.Model.CategoryModel;
import vn.iot.star.Service.ICategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

	@Autowired
	ICategoryService categoryService;

	// Danh sách tất cả (không phân trang) - không dùng nữa
	@GetMapping({ "", "/", "/list" })
	public String list(ModelMap model) {
		model.addAttribute("categories", categoryService.findAll());
		return "admin/categories/list";
	}

	// Thêm danh mục
	@GetMapping("/add")
	public String add(ModelMap model) {
		CategoryModel categoryModel = new CategoryModel();
		categoryModel.setIsEdit(false);
		model.addAttribute("category", categoryModel);
		return "admin/categories/addOrEdit";
	}

	// Sửa danh mục
	@GetMapping("/edit/{categoryId}")
	public String edit(@PathVariable("categoryId") Long categoryId, ModelMap model) {
		Optional<CategoryEntity> opt = categoryService.findById(categoryId);
		if (opt.isPresent()) {
			CategoryModel categoryModel = new CategoryModel();
			BeanUtils.copyProperties(opt.get(), categoryModel);
			categoryModel.setIsEdit(true);
			model.addAttribute("category", categoryModel);
			return "admin/categories/addOrEdit";
		}
		model.addAttribute("message", "Category not found!");
		return "redirect:/admin/categories/searchpaginated";
	}

	// Lưu hoặc cập nhật
	@PostMapping("/saveOrUpdate")
	public String saveOrUpdate(@ModelAttribute("category") CategoryModel categoryModel, RedirectAttributes redirect) {
		CategoryEntity entity = new CategoryEntity();
		BeanUtils.copyProperties(categoryModel, entity);
		categoryService.save(entity);

		redirect.addFlashAttribute("message",
				Boolean.TRUE.equals(categoryModel.getIsEdit()) ? "Edited successfully!" : "Added successfully!");
		return "redirect:/admin/categories/searchpaginated";
	}

	// Xoá danh mục
	@GetMapping("/delete/{categoryId}")
	public String delete(@PathVariable("categoryId") Long categoryId, RedirectAttributes redirect) {
		categoryService.deleteById(categoryId);
		redirect.addFlashAttribute("message", "Deleted successfully!");
		return "redirect:/admin/categories/searchpaginated";
	}

	// Tìm kiếm & phân trang
	@GetMapping("/searchpaginated")
	public String searchPaginated(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
		Page<CategoryEntity> resultPage = StringUtils.hasText(name)
				? categoryService.findByNameContaining(name, pageable)
				: categoryService.findAll(pageable);

		model.addAttribute("name", name);
		model.addAttribute("categoryPage", resultPage);

		int totalPages = resultPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);
			if (totalPages > 5) {
				if (end == totalPages)
					start = end - 5;
				else if (start == 1)
					end = start + 5;
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().toList();
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "admin/categories/searchpaginated";
	}
}
