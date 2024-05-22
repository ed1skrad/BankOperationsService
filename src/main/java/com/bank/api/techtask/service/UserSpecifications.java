package com.bank.api.techtask.service;

import com.bank.api.techtask.domain.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserSpecifications {

    public Specification<User> hasDateOfBirthAfter(Date dateOfBirth) {
        return (root, query, criteriaBuilder) ->
                dateOfBirth == null ? null : criteriaBuilder.greaterThan(root.get("dateOfBirth"), dateOfBirth);
    }

    public Specification<User> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) ->
                phoneNumber == null ? null : criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
    }

    public Specification<User> hasFullNameStartingWith(String fullName) {
        return (root, query, criteriaBuilder) ->
                fullName == null ? null : criteriaBuilder.like(root.get("fullName"), fullName + "%");
    }

    public Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.equal(root.get("email"), email);
    }
}
