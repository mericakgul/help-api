package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.UserExistence;
import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.AddressRepository;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final DtoMapper dtoMapper;
    private final UserExistence userExistence;

    public List<AddressDto> findAll() {
        List<Address> addresses = this.addressRepository.findAll();
        if (addresses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no address saved yet.");
        } else {
            return this.dtoMapper.mapListModel(addresses, AddressDto.class);
        }
    }
    public List<AddressDto> findAddressesByUsername(String username){
        User user = userExistence.checkIfUserExistsAndReturn(username);
        List<Address> addressesOfUser = this.addressRepository.findAddressesByUserUuid(user.getUserUuid());
        return this.dtoMapper.mapListModel(addressesOfUser, AddressDto.class);
    }

    @Transactional
    public List<Address> saveAll(List<Address> addressesRequest) {
        return this.addressRepository.saveAll(addressesRequest);
    }

    @Transactional
    public void deleteRelatedAddresses(UUID userUuid) {
        List<Address> relatedAddresses = this.addressRepository.findAddressesByUserUuid(userUuid);
        if (!relatedAddresses.isEmpty()) {
            relatedAddresses.forEach(address -> {
                address.setDeletedDate(new Date());
                this.addressRepository.save(address);
            });
        }
    }

    @Transactional
    public AddressDto saveAddressByUsername(String username, AddressDto addressRequest) {
        User user = userExistence.checkIfUserExistsAndReturn(username);
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(Objects.equals(loggedInUsername, username)){
            Address addressToSave = this.dtoMapper.mapModel(addressRequest, Address.class);
//            Address savedAddress = this.addressRepository.save(addressToSave);

//            Note: We do not have to save address because we added cascade to addresses property of User entity.
//            And because user variable above is a persist object, any change would be reflected to DB.
//            Therefore, when we add an address to a user it will be automatically reflected to DB and because w have cascade,
//            the new address would be saved to Address table automatically.

            user.getAddresses().add(addressToSave);
            return this.dtoMapper.mapModel(addressToSave, AddressDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorised to add address to the user: " + username);
        }
    }
}
