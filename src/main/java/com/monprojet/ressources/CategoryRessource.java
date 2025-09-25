package com.monprojet.ressources;

import com.monprojet.models.Category;
import com.monprojet.services.CategoryServices;
import jakarta.ws.rs.*;

import java.util.List;

@Path("/categories")
public class CategoryRessource {

    private final CategoryServices service = new CategoryServices();

    @GET
    public List<Category> getAll() {
        return service.getAllCategories();
    }
    
}
