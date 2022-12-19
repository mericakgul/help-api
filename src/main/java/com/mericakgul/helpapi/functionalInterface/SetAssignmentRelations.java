package com.mericakgul.helpapi.functionalInterface;

import com.mericakgul.helpapi.model.dto.AssignmentRequest;
import com.mericakgul.helpapi.model.dto.AssignmentResponse;
import com.mericakgul.helpapi.model.entity.User;

// Not being used for now, added for practice purposes.
@FunctionalInterface
public interface SetAssignmentRelations {
    AssignmentResponse setRelations(AssignmentRequest assignmentRequest,
                                    String loggedInUser,
                                    User serviceProviderUser);
}
