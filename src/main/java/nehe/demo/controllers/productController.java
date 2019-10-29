package nehe.demo.controllers;



import java.io.IOException;
import java.util.List;


import com.google.gson.Gson;
import nehe.demo.Modals.Product;
import nehe.demo.Services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class productController {

	private ProductService productService;

	private static final Gson gson = new Gson();

	@Autowired
	public productController(ProductService productService)
	{
		this.productService = productService;
	}

	//get all products
	@GetMapping("/allProducts")
	public List<Product> getAllProducts()
	{
		return productService.getAllProducts();
	}

    //add product
	@PostMapping("/addProduct")
	public ResponseEntity<String> addProduct(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException
	{
		//check if the product already exists in db
		if(productService.checkIfProductExists(request.getHeader("ProductName")))
		{
		  return 	ResponseEntity.ok(gson.toJson("Product exists !"));
		}
       productService.addProductOrUpdateProduct1(file,request);

       return ResponseEntity.ok(gson.toJson("Product saved"));
	}

	//delete a product
	@DeleteMapping("deleteProduct/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") int id)
	{
		productService.deleteProduct(id);
		return ResponseEntity.ok(gson.toJson("Product deleted"));
	}

    //edit product
    //when request comes with a file
    @PostMapping("/editProduct1")
    public ResponseEntity<String> editProduct(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException
    {
        productService.addProductOrUpdateProduct1(file,request);

        return ResponseEntity.ok(gson.toJson("Changes made successfully"));
    }

    //edit product
    //when request doesn't come with a file
    //file is included as bytes in Product object
    @PostMapping("/editProduct2")
    public ResponseEntity<String> editProduct(@RequestBody Product product)
    {
        productService.addProductOrUpdateProduct2(product);

        return ResponseEntity.ok(gson.toJson("Changes made successfully"));
    }



}//controller class






