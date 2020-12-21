package com.example.demo.services;

import com.example.demo.exceptions.InvalidEntityException;

public interface AdminService {
    void deleteItem(Long Id) throws InvalidEntityException;
}
