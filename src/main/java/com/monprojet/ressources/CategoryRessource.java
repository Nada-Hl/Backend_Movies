package com.monprojet.ressources;

import com.monprojet.models.Category;
import com.monprojet.services.CategoryServices;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/categories")
public class CategoryRessource {

    private final CategoryServices service = new CategoryServices();

    @GET
    public List<Category> getAll() {
        return service.getAllCategories();
    }

    @POST
    public Response create(Category category) {
        if (category == null || category.getCategoryName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("category_name required").build();
        }
        Category created = service.createCategory(category.getCategoryName());
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
}
