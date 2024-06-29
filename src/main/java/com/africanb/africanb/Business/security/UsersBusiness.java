package com.africanb.africanb.Business.security;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.Gare;
import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.GareRepository;
import com.africanb.africanb.dao.repository.security.RoleFunctionalityRepository;
import com.africanb.africanb.dao.repository.security.RoleRepository;
import com.africanb.africanb.dao.repository.security.UsersRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import com.africanb.africanb.helper.dto.security.UsersPassWordDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.security.UsersTransformer;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import com.africanb.africanb.utils.security.JwtUtils;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Log
@Component
public class UsersBusiness implements IBasicBusiness<Request<UsersDTO>, Response<UsersDTO>> {


    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final GareRepository gareRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final RoleFunctionalityRepository roleFunctionalityRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public UsersBusiness(UsersRepository usersRepository, RoleRepository roleRepository, GareRepository gareRepository, CompagnieTransportRepository compagnieTransportRepository, RoleFunctionalityRepository roleFunctionalityRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.gareRepository = gareRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.roleFunctionalityRepository = roleFunctionalityRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<UsersDTO> login(Request<UsersDTO> request, Locale locale) throws Exception {
        log.info("----begin login-----");
        Response<UsersDTO> response = new Response<>();
        UsersDTO dto = request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("login", dto.getLogin());
        fieldsToVerify.put("password", dto.getPassword());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        Users existingEntity = usersRepository.findByLoginAndPassword(dto.getLogin(), JwtUtils.encryptPassword(dto.getPassword()), false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("login or password is not correct -> " + dto.getLogin() + " " + dto.getPassword(), locale));
            response.setHasError(true);
            return response;
        }
        if(existingEntity.getNumberOfConnections() == null){
            existingEntity.setNumberOfConnections(0L);
        }
        if(existingEntity.getNumberOfConnections()>1){
            Long compt=existingEntity.getNumberOfConnections() + 1 ;
            existingEntity.setNumberOfConnections(compt);
        }
        UsersDTO existingEntityDto = UsersTransformer.INSTANCE.toDto(existingEntity);
        if (existingEntityDto.getLastConnectionDate() != null && !existingEntityDto.getLastConnectionDate().isEmpty()) {
            existingEntityDto.setLastConnection(existingEntityDto.getLastConnectionDate());
        }
        existingEntity.setLastConnectionDate(Utilities.getCurrentDate());
        usersRepository.save(existingEntity);
        String token = JwtUtils.generateToken(existingEntity);
        existingEntityDto.setToken(token);
        response.setItem(existingEntityDto);
        response.setHasError(Boolean.FALSE);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end login-----");
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<UsersDTO> logout(@RequestBody Request<UsersDTO> request,Locale locale) {
        Response<UsersDTO> response = new Response<>();
        UsersDTO usersDTO = new UsersDTO();
        response.setItem(usersDTO);
        response.setHasError(Boolean.FALSE);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<UsersDTO> resetPassWordUser(Request<UsersPassWordDTO> request, Locale locale) {
        Response<UsersDTO> response = new Response<>();
        UsersPassWordDTO dto = request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("email", dto.getEmail());
        fieldsToVerify.put("newPassWord", dto.getNewPassWord());
        fieldsToVerify.put("oldPassWord", dto.getOldPassWord());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        Users existingEntity = usersRepository.findByEmail(dto.getEmail(), false); //
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Utilisateur inexistant -> " + dto.getEmail(), locale));
            response.setHasError(true);
            return response;
        }
        String oldPassWordEncryted=existingEntity.getPassword();
        if(!oldPassWordEncryted.equals(JwtUtils.encryptPassword(dto.getOldPassWord()))){
            response.setStatus(functionalError.SAVE_FAIL("Ancien mot de passe incorrect !!!", locale));
            response.setHasError(true);
            return response;
        }
        String newPassWordEncryted= JwtUtils.encryptPassword(dto.getNewPassWord());
        if(oldPassWordEncryted.equals(newPassWordEncryted)){
            response.setStatus(functionalError.SAVE_FAIL("Ancien et nouveau mot de passe identique !!!", locale));
            response.setHasError(true);
            return response;
        }
        existingEntity.setPassword(newPassWordEncryted);
        if(existingEntity.getNumberOfConnections()==0 && (!Objects.equals(JwtUtils.encryptPassword(ProjectConstants.USER_PASSWORD_DEFAULT), oldPassWordEncryted))){
            existingEntity.setNumberOfConnections(existingEntity.getNumberOfConnections()+1);
        }
        Users savinEntity = usersRepository.save(existingEntity);
        UsersDTO existingEntityDto = UsersTransformer.INSTANCE.toDto(savinEntity);
        if (existingEntityDto.getLastConnectionDate() != null && !existingEntityDto.getLastConnectionDate().isEmpty()) {
            existingEntityDto.setLastConnection(existingEntityDto.getLastConnectionDate());
        }
        response.setItem(existingEntityDto);
        response.setHasError(Boolean.FALSE);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end resetPassWordUser-----");
        return response;
    }

    @Override
    public Response<UsersDTO> create(Request<UsersDTO> request, Locale locale) throws ParseException {
        log.info("----begin create Users-----");
        Response<UsersDTO> response = new Response<>();
        List<Users> items = new ArrayList<>();
        for (UsersDTO dto : request.getDatas()) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("nom", dto.getNom());
            fieldsToVerify.put("prenoms", dto.getPrenoms());
            fieldsToVerify.put("login", dto.getLogin());
            fieldsToVerify.put("email", dto.getEmail());
            fieldsToVerify.put("roleCode", dto.getRoleCode());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if (items.stream().anyMatch(a -> a.getLogin().equalsIgnoreCase(dto.getLogin()))) {
                response.setStatus(functionalError.DATA_DUPLICATE(" login ", locale));
                response.setHasError(true);
                return response;
            }
            if(items.stream().anyMatch(a -> a.getMatricule().equalsIgnoreCase(dto.getEmail()))) {
                response.setStatus(functionalError.DATA_DUPLICATE(" Email ", locale));
                response.setHasError(true);
                return response;
            }
            Users existingEntity = usersRepository.findByLogin(dto.getLogin(), false);
            if (existingEntity != null) {
                response.setStatus(functionalError.SAVE_FAIL("Utilisateur existe !!!" + dto.getLogin(), locale));
                response.setHasError(true);
                return response;
            }
            if(dto.getRoleCode().equalsIgnoreCase(ProjectConstants.ROLE_UTI_GARE_COMPAGNIE_TRANSPORT) && dto.getGareDesignation()==null){
                response.setStatus(functionalError.SAVE_FAIL("Gare utilisateur non indiquée !!!", locale));
                response.setHasError(true);
                return response;
            }else if (dto.getRoleCode().equalsIgnoreCase(ProjectConstants.ROLE_UTI_GARE_COMPAGNIE_TRANSPORT) && dto.getGareDesignation()!=null){
                Gare existingGare = gareRepository.findByDesignation(dto.getGareDesignation(),false);
                if (existingGare == null) {
                    response.setStatus(functionalError.SAVE_FAIL("Gare utilisateur inexistante !!!", locale));
                    response.setHasError(true);
                    return response;
                }
            }

            CompagnieTransport existingCompagnieTransport = null;
            if(dto.getCompagnieTransportRaisonSociale() != null){
                existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(dto.getCompagnieTransportRaisonSociale(), Boolean.FALSE);
                if (existingCompagnieTransport == null) {
                    response.setStatus(functionalError.SAVE_FAIL("Compagnie Transport inexistante !!!", locale));
                    response.setHasError(true);
                    return response;
                }
            }
            Role existingRole;
            existingRole = roleRepository.findByCode(dto.getRoleCode(), false);
            if (existingRole == null) {
                response.setStatus(functionalError.SAVE_FAIL("Role inexistant !!!", locale));
                response.setHasError(true);
                return response;
            }
            Users entityToSave = UsersTransformer.INSTANCE.toEntity(dto,existingRole,existingCompagnieTransport);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            entityToSave.setIsFirst(Boolean.TRUE);
            entityToSave.setIsActif(Boolean.FALSE);
            String generateNewPassword = JwtUtils.generatePassword();
            try {
                entityToSave.setPassword(JwtUtils.encryptPassword(ProjectConstants.USER_PASSWORD_DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }
            items.add(entityToSave);
        }
        if(items.isEmpty()){
            response.setStatus(functionalError.SAVE_FAIL("Liste vide", locale));
            response.setHasError(true);
            return response;
        }
        List<Users> itemsSaved = usersRepository.saveAll(items);
        List<UsersDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? UsersTransformer.INSTANCE.toLiteDtos(itemsSaved) : UsersTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<UsersDTO> update(Request<UsersDTO> request, Locale locale) throws ParseException {
        Response<UsersDTO> response = new Response<>();
        List<Users> items = new ArrayList<>();
        for (UsersDTO dto : request.getDatas()) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Users entityToSave = usersRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Users id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            Long entityToSaveId = entityToSave.getId();
            if (Utilities.isNotBlank(dto.getLogin()) && !dto.getLogin().equals(entityToSave.getLogin())) { //verify login
                Users existingEntity = usersRepository.findByLogin(dto.getLogin(), false);
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Aucun utilisateur trouvé " + dto.getLogin(), locale));
                    response.setHasError(true);
                    return response;
                }
                if (items.stream().anyMatch(a -> a.getLogin().equalsIgnoreCase(dto.getLogin()) && !a.getId().equals(entityToSaveId))) {
                    response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du login '" + dto.getLogin() + "' pour les users", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setLogin(dto.getLogin());
            }
            if (Utilities.isNotBlank(dto.getEmail()) && !dto.getEmail().equals(entityToSave.getEmail())) { //verify Matricule
                Users existingEntity = usersRepository.findByMatricule(dto.getEmail(), false);
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Aucun utilisateur trouvé " + dto.getEmail(), locale));
                    response.setHasError(true);
                    return response;
                }
                if (items.stream().anyMatch(a -> a.getEmail().equalsIgnoreCase(dto.getEmail()) && !a.getId().equals(entityToSaveId))) {
                    response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du Matricule '" + dto.getEmail() + "' pour les users", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setMatricule(dto.getEmail());
            }
            if (Utilities.isNotBlank(dto.getPassword()) && !dto.getEmail().equals(entityToSave.getPassword())) {
                if (Utilities.isNotBlank(JwtUtils.encryptPassword(dto.getPassword())) && !Objects.equals(JwtUtils.encryptPassword(dto.getPassword()), entityToSave.getPassword())) {
                    try {
                        entityToSave.setPassword(JwtUtils.encryptPassword(dto.getPassword()));
                        if (entityToSave.getIsFirst() != null && entityToSave.getIsFirst()) {
                            entityToSave.setIsFirst(Boolean.FALSE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (Utilities.isNotBlank(dto.getNom()) && !dto.getNom().equals(entityToSave.getNom())) {
                entityToSave.setNom(dto.getNom());
            }
            if (Utilities.isNotBlank(dto.getPrenoms()) && !dto.getPrenoms().equals(entityToSave.getPrenoms())) {
                entityToSave.setPrenoms(dto.getPrenoms());
            }
            if (dto.getIsActif() != null && !dto.getIsActif().equals(entityToSave.getIsActif())) {
                entityToSave.setIsActif(dto.getIsActif());
            }
            if (Utilities.isNotBlank(dto.getTelephone()) && !dto.getTelephone().equals(entityToSave.getTelephone())) {
                entityToSave.setTelephone(dto.getTelephone());
            }
            if (Utilities.isValidID(dto.getRoleId()) && !entityToSave.getRole().getId().equals(dto.getRoleId())) {
                Role existingRole = roleRepository.findOne(dto.getRoleId(), false);
                if (existingRole == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("role roleId -> " + dto.getRoleId(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setRole(existingRole);
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if (!items.isEmpty()) {
            List<Users> itemsSaved = usersRepository.saveAll(items);
            List<UsersDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? UsersTransformer.INSTANCE.toLiteDtos(itemsSaved) : UsersTransformer.INSTANCE.toDtos(itemsSaved);
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("end users update");
        return response;
    }

    @Override
    public Response<UsersDTO> delete(Request<UsersDTO> request, Locale locale) {
        Response<UsersDTO> response = new Response<>();
        List<Users> items = new ArrayList<>();
        for (UsersDTO dto : request.getDatas()) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Users existingEntity = usersRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Users id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            items.add(existingEntity);
        }
        if (!items.isEmpty()) {
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end delete Users-----");
        return response;
    }

    @Override
    public Response<UsersDTO> forceDelete(Request<UsersDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<UsersDTO> getAll(Locale locale) throws ParseException {
        Response<UsersDTO> response = new Response<>();
        List<Users> users = usersRepository.findByIsDeleted(false);
        List<UsersDTO> usersDtos = UsersTransformer.INSTANCE.toDtos(users);
        response.setItems(usersDtos);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("",locale));
        log.info("----end get role -----");
        return response;
    }

    @Override
    public Response<UsersDTO> getByCriteria(Request<UsersDTO> request, Locale locale) {
        return null ;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<UsersDTO> activeUser(Request<UsersDTO> request, Locale locale) {
        Response<UsersDTO> response = new Response<>();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        if(request.getData() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée définie ",locale));
            response.setHasError(true);
            return response;
        }
        fieldsToVerify.put("login",request.getData().getLogin());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        Users existingEntity = usersRepository.findByLogin(request.getData().getLogin(),false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Utilisateur inexistant", locale));
            response.setHasError(true);
            return response;
        }
        existingEntity.setIsActif(Boolean.TRUE);
        UsersDTO itemDto = UsersTransformer.INSTANCE.toDto(existingEntity);
        response.setItem(itemDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

}
