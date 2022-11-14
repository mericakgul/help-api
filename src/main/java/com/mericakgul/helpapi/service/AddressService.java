package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.ObjectUpdaterHelper;
import com.mericakgul.helpapi.core.helper.UserExistence;
import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final DtoMapper dtoMapper;
    private final UserExistence userExistence;
    private final ObjectUpdaterHelper objectUpdaterHelper;

    public List<AddressDto> findAll() {
        List<Address> addresses = this.addressRepository.findAll();
        if (addresses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no address saved yet.");
        } else {
            return this.dtoMapper.mapListModel(addresses, AddressDto.class);
        }
    }

    public List<AddressDto> findAddressesByUsername(String username) {
        User user = userExistence.checkIfUserExistsAndReturn(username);
        List<Address> addressesOfUser = this.addressRepository.findAddressesByUserUuid(user.getUserUuid());
        return this.dtoMapper.mapListModel(addressesOfUser, AddressDto.class);
    }

    @Transactional
    public List<Address> saveAll(List<Address> addressesRequest) {
        return this.addressRepository.saveAll(addressesRequest);
    }

    @Transactional
    public AddressDto saveAddressByUsername(String username, AddressDto addressRequest) {
        User user = userExistence.checkIfUserExistsAndReturn(username);
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.equals(loggedInUsername, username)) {
            Address addressToSave = this.dtoMapper.mapModel(addressRequest, Address.class);
/*            Address savedAddress = this.addressRepository.save(addressToSave);

            Note: We do not have to save address because we added cascade to addresses property of User entity.
            And because user variable above is a persist object, any change would be reflected to DB.
            Therefore, when we add an address to a user it will be automatically reflected to DB and because w have cascade,
           the new address would be saved to Address table automatically.
*/
            user.getAddresses().add(addressToSave);
            return this.dtoMapper.mapModel(addressToSave, AddressDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorised to add address to the user: " + username);
        }
    }

    @Transactional
    public AddressDto updateAddressByFields(Integer houseNumber, String zipCode, String city, String Country, AddressDto upToDateAddress) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Address currentAddress = this.findAddressByFieldsAndUsername(houseNumber, zipCode, city, Country, loggedInUsername);
        this.objectUpdaterHelper.updateAddressObjectPrimitiveFields(currentAddress, upToDateAddress);
        //No need to save the currentAddress to repo since it is doing it automatically because currentAddress is an object fetched from repo..
        return this.dtoMapper.mapModel(currentAddress, AddressDto.class);
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
    public void deleteAddressByFields(Integer houseNumber, String zipCode, String city, String Country) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Address addressAboutToDelete = this.findAddressByFieldsAndUsername(houseNumber, zipCode, city, Country, loggedInUsername);
        checkIfUserHasAnotherAddressAndDelete(addressAboutToDelete);
    }

    private void checkIfUserHasAnotherAddressAndDelete(Address addressAboutToDelete) {
        if (addressAboutToDelete.getUser().getAddresses().size() > 1) {
            addressAboutToDelete.setDeletedDate(new Date());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This address cannot be deleted because the user doesn't have any other address. Try to update or add another address.");
        }
    }

    private Address findAddressByFieldsAndUsername(Integer houseNumber, String zipCode, String city, String Country, String loggedInUsername) {
        List<Address> addresses = this.addressRepository
                .findAddressByHouseNumberAndZipCodeAndCityAndCountry(houseNumber, zipCode, city, Country);

        Optional<Address> addressOfLoggedInUser = addresses.stream()
                .filter(address -> address.getUser().getUsername().equals(loggedInUsername))
                .findAny();
        /*
        While saving addresses we are not checking if the same one already exist or not but just saving the address
        with the same fields but different id's. In this case two different accounts may save the same address to their account but
        these two same addresses are kept with different id's. When it comes to delete or update an address by the
        fields (houseNumber, zipCode, city and country) we might fetch more than one address because of the reason explained above, so
        we are also filtering these addresses by username. Only downside of this way is that a user is also allowed to save two same
        addresses in their account. But in this case; if the user wants to update or delete an address then the first one is graped,
        and we don't get any error.
         */
        if(addressOfLoggedInUser.isPresent()){
            return addressOfLoggedInUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have this address saved in your account so you cannot delete or update it.");
        }
    }
}
