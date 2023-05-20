package com.africanb.africanb.helper.transformer.compagnie.ModePaiement;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementWave;
import com.africanb.africanb.helper.contrat.FullTransformerQualifier;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementWaveDTO;
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
public interface ModePaimentWaveTransformer {


    ModePaimentWaveTransformer INSTANCE = Mappers.getMapper(ModePaimentWaveTransformer.class);

    @FullTransformerQualifier
    @Mappings({
            @Mapping(source = "entity.id", target = "id"),
            @Mapping(source = "entity.designation", target = "designation"),
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.telephoneWave", target = "telephoneWave"),

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
    ModePaiementWaveDTO toDto(ModePaiementWave entity) throws ParseException;;

    @IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ModePaiementWaveDTO> toDtos(List<ModePaiementWave> entities) throws ParseException;

    default ModePaiementWaveDTO toLiteDto(ModePaiementWave entity) {
        if (entity == null) {
            return null;
        }
        ModePaiementWaveDTO dto = new ModePaiementWaveDTO();
        dto.setId( entity.getId() );
        dto.setDesignation( entity.getDesignation() );
        dto.setDescription(entity.getDescription());
        dto.setTelephoneWave(entity.getTelephoneWave());
        return dto;
    }

    default List<ModePaiementWaveDTO> toLiteDtos(List<ModePaiementWave> entities) {
        if (entities == null || entities.stream().allMatch(o -> o == null)) {
            return null;
        }
        List<ModePaiementWaveDTO> dtos = new ArrayList<ModePaiementWaveDTO>();
        for (ModePaiementWave entity : entities) {
            dtos.add(toLiteDto(entity));
        }
        return dtos;
    }

    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.designation", target = "designation"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.telephoneWave", target = "telephoneWave"),

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
    ModePaiementWave toEntity(ModePaiementWaveDTO dto, CompagnieTransport compagnieTransport, Reference typeModePaiement) throws ParseException;
}
