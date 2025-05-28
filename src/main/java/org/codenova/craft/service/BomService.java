package org.codenova.craft.service;

import lombok.AllArgsConstructor;
import org.codenova.craft.entity.Bom;
import org.codenova.craft.repository.BomRepository;
import org.codenova.craft.response.BomNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BomService {

    private final BomRepository bomRepository;

    public BomNode convertToBomNode(Bom bom) {
        List<Bom> childBom = bomRepository.findByParentProduct(bom.getChildProduct());
        List<BomNode> childBomNodes = new ArrayList<>();
        for(Bom bomChild : childBom ) {
            childBomNodes.add(convertToBomNode(bomChild));
        }

        BomNode node = BomNode.builder().
                id(bom.getId().toString())
                .label(bom.getParentProduct().getName())
                .children(childBomNodes)
                .build();

        return node;
    }

}
