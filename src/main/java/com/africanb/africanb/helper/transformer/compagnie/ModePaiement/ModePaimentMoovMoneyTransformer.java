package com.africanb.africanb.helper.transformer.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMoovMoney;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementMoovMoneyDTO;
import com.africanb.africanb.utils.Reference.Reference;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper
public interface ModePaimentMoovMoneyTransformer {


    ModePaimentMoovMoneyTransformer INSTANCE = Mappers.getMapper(ModePaimentMoovMoneyTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.telephoneMoovMoney", target = "telephoneMoovMoney"),

            @Mapping(source = "entity.compagnieTransport.raisonSociale", target = "compagnieTransportRaisonSociale"),
            @Mapping(source = "entity.typeModePaiement.designation", target = "typeModePaiementDesignation"),

            @Mapping(source = "entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "entity.updatedBy", target="updatedBy"),
            @Mapping(source = "entity.createdBy", target="createdBy"),
            @Mapping(source = "entity.deletedBy", target="deletedBy"),
            @Mapping(source = "entity.isDeleted", target="isDeleted"),
    })
    ModePaiementMoovMoneyDTO toDto(ModePaiementMoovMoney entity) throws ParseException;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ModePaiementMoovMoneyDTO> toDtos(List<ModePaiementMoovMoney> entities) throws ParseException;

    default ModePaiementMoovMoneyDTO toLiteDto(ModePaiementMoovMoney entity) {
        if (entity == null) {
            return null;
        }
        ModePaiementMoovMoneyDTO dto = new ModePaiementMoovMoneyDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setTelephoneMoovMoney(entity.getTelephoneMoovMoney());
        return dto;
    }

    default List<ModePaiementMoovMoneyDTO> toLiteDtos(List<ModePaiementMoovMoney> entities) {
        if (entities == null || entities.stream().allMatch(Objects::isNull)) {
            return null;
        }
        List<ModePaiementMoovMoneyDTO> dtos = new ArrayList<>();
        for (ModePaiementMoovMoney entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.telephoneMoovMoney", target = "telephoneMoovMoney"),

            @Mapping(source = "dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
            @Mapping(source = "dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
            @Mapping(source = "dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
            @Mapping(source = "dto.updatedBy", target="updatedBy"),
            @Mapping(source = "dto.createdBy", target="createdBy"),
            @Mapping(source = "dto.deletedBy", target="deletedBy"),
            @Mapping(source = "dto.isDeleted", target="isDeleted"),

            @Mapping(source = "compagnieTransport", target="compagnieTransport"),
            @Mapping(source = "typeModePaiement", target="typeModePaiement"),
    })
    ModePaiementMoovMoney toEntity(ModePaiementMoovMoneyDTO dto, CompagnieTransport compagnieTransport, Reference typeModePaiement) throws ParseException;
}
