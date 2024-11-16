package com.tsm.repository.specification;

import com.tsm.entity.Task;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class TaskSpecification {

    public static Specification<Task> hasTitleOrDescriptionOrStatus(String title, String description, String status) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Predicate titlePredicate = StringUtils.hasText(title) ? builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%") : null;
            Predicate descriptionPredicate = StringUtils.hasText(description) ? builder.like(builder.lower(root.get("description")), "%" + description.toLowerCase() + "%") : null;
            Predicate statusPredicate = StringUtils.hasText(status) ? builder.like(builder.lower(root.get("status")), "%" + status.toLowerCase() + "%") : null;
            
            Predicate finalPredicate = builder.or(titlePredicate, descriptionPredicate, statusPredicate);
            
            return finalPredicate;
        };
    }

    public static Specification<Task> hasPriority(String priority) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return StringUtils.hasText(priority) ? builder.equal(root.get("priority"), priority) : null;
        };
    }

    public static Specification<Task> hasAssigneeId(Long assigneeId) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return assigneeId != null ? builder.equal(root.get("assigneeId"), assigneeId) : null;
        };
    }

    public static Specification<Task> searchByFilter(String filter) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (StringUtils.hasText(filter)) {
                Predicate titlePredicate = builder.like(builder.lower(root.get("title")), "%" + filter.toLowerCase() + "%");
                Predicate descriptionPredicate = builder.like(builder.lower(root.get("description")), "%" + filter.toLowerCase() + "%");
                Predicate statusPredicate = builder.like(builder.lower(root.get("status")), "%" + filter.toLowerCase() + "%");

                return builder.or(titlePredicate, descriptionPredicate, statusPredicate);
            }
            return builder.conjunction(); 
        };
    }

}
