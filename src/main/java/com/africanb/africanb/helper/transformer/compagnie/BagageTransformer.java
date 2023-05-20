package com.africanb.africanb.helper.transformer.compagnie;


import com.africanb.africanb.dao.entity.compagnie.Bagage;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.BagageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.PrixOffreVoyageDTO;
import com.africanb.africanb.utils.Reference.Reference;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface BagageTransformer {

    BagageTransformer INSTANCE = Mappers.getMapper(BagageTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.coutBagageParTypeBagage", target = "coutBagageParTypeBagage"),
            @Mapping(source = "entity.nombreBagageGratuitParTypeBagage", target = "nombreBagageGratuitParTypeBagage"),

            @Mapping(source = "entity.typeBagage.designation", target = "typeBagageDesignation"),
            @Mapping(source = "entity.compagnieTransport.raisonSociale", target = "compagnieTransportRaisonSociale"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    BagageDTO toDto(Bagage entity) throws ParseException;;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<BagageDTO> toDtos(List<Bagage> entities) throws ParseException;

    default BagageDTO toLiteDto(Bagage entity) {
        if (entity == null) {
            return null;
        }
        BagageDTO dto = new BagageDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setCoutBagageParTypeBagage(entity.getCoutBagageParTypeBagage());
        dto.setNombreBagageGratuitParTypeBagage(entity.getNombreBagageGratuitParTypeBagage());
        return dto;
    }

    default List<BagageDTO> toLiteDtos(List<Bagage> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<BagageDTO> dtos = new ArrayList<BagageDTO>();
        for (Bagage entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.coutBagageParTypeBagage", target = "coutBagageParTypeBagage"),
            @Mapping(source = "dto.nombreBagageGratuitParTypeBagage", target = "nombreBagageGratuitParTypeBagage"),

            @Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source="dto.updatedBy", target="updatedBy"),
            @Mapping(source="dto.createdBy", target="createdBy"),
            @Mapping(source="dto.deletedBy", target="deletedBy"),
            @Mapping(source="dto.isDeleted", target="isDeleted"),

            @Mapping(source="typeBagage", target="typeBagage"),
            @Mapping(source="compagnieTransport", target="compagnieTransport")
    })
    Bagage toEntity(BagageDTO dto, Reference typeBagage, CompagnieTransport compagnieTransport) throws ParseException;
}
