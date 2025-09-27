package vn.iot.star.Service;

import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Example;
import vn.iot.star.Entity.CategoryEntity;

public interface ICategoryService {
	<S extends CategoryEntity> S save(S entity);

	List<CategoryEntity> findAll();

	Page<CategoryEntity> findAll(Pageable pageable);

	List<CategoryEntity> findAll(Sort sort);

	List<CategoryEntity> findAllById(Iterable<Long> ids);

	Optional<CategoryEntity> findById(Long id);

	<S extends CategoryEntity> Optional<S> findOne(Example<S> example);

	long count();

	void deleteById(Long id);

	void delete(CategoryEntity entity);

	void deleteAll();

	List<CategoryEntity> findByNameContaining(String name);

	Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);
}
