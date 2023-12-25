package akletini.life.core.product.controller;

import akletini.life.core.product.dto.ProductDto;
import akletini.life.core.product.dto.mapper.ProductMapper;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.service.ProductService;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping(value = "/add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) throws BusinessException {
        Product product = productMapper.dtoToProduct(productDto);
        Product storedProduct = productService.store(product);
        return ResponseEntity.status(OK).body(productMapper.productToDto(storedProduct));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) throws BusinessException {
        Product product = productMapper.dtoToProduct(productDto);
        productService.getById(product.getId());
        Product updatedproduct = productService.store(product);
        return ResponseEntity.status(OK).body(productMapper.productToDto(updatedproduct));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ProductDto> deleteProduct(@RequestBody ProductDto productDto) {
        Product product = productMapper.dtoToProduct(productDto);
        productService.delete(product);
        return ResponseEntity.status(OK).build();
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) throws EntityNotFoundException {
        Product productById = productService.getById(id);
        return ResponseEntity.status(OK).body(productMapper.productToDto(productById));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<ProductDto>> getAll() {
        List<Product> all = productService.getAll();
        return ResponseEntity.status(OK).body(all.stream().map(productMapper::productToDto).collect(Collectors.toList()));
    }
}
