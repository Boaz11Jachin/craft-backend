package org.codenova.craft.controller;


import lombok.RequiredArgsConstructor;
import org.codenova.craft.entity.Bom;
import org.codenova.craft.entity.Product;
import org.codenova.craft.repository.BomRepository;
import org.codenova.craft.repository.ProductRepository;
import org.codenova.craft.response.BomNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final BomRepository bomRepository;

    @GetMapping("/api/product")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products  = productRepository.findAll();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("products", products);
        response.put("total", products.size());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/api/product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {

        Product product = productRepository.findById(productId).orElseThrow();


        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("product", product);

        List<Bom> bomList = bomRepository.findByParentProduct(product);

        List<BomNode> bomNodeList =bomList.stream().map( (bom) -> {
            List<Bom> cilldBomList = bomRepository.findByParentProduct(bom.getChildProduct());
            List<BomNode> children = cilldBomList.stream().map( (child) -> {
                return BomNode.builder()
                        .id(child.getId().toString())
                        .label(child.getChildProduct().getName() +" x " + child.getQuantity())
                        .build();
            }).toList();

            return BomNode.builder()
                    .id(bom.getId().toString())
                    .label(bom.getChildProduct().getName() +" x " + bom.getQuantity())
                    .children(children)
                    .build();
        }).toList();

        response.put("bomTree", bomNodeList);


        return ResponseEntity.status(200).body(response);
    }



}
