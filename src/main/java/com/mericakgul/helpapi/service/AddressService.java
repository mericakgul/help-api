package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final DtoMapper dtoMapper;

    public List<AddressDto> findAll() {
        List<Address> addresses = this.addressRepository.findAll();
        if (addresses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no address saved yet.");
        } else {
            return this.dtoMapper.mapListModel(addresses, AddressDto.class);
        }
    }

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
