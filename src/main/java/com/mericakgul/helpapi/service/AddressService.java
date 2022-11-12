package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.AddressRepository;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    public List<AddressDto> findAll() {
        List<Address> addresses = this.addressRepository.findAll();
        if (addresses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no address saved yet.");
        } else {
            return this.dtoMapper.mapListModel(addresses, AddressDto.class);
        }
    }
    public List<AddressDto> findAddressesByUsername(String username){
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with this username"));
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
}
