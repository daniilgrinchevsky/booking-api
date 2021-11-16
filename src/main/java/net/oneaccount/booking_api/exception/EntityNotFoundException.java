package net.oneaccount.booking_api.exception;

import lombok.RequiredArgsConstructor;

public class EntityNotFoundException extends RuntimeException {

    //This should be located in separate util class with error messages as constants, or in prop file in case of i18n f.e.
    public static final String MESSAGE = "Entity with id = %d not found";

    //Here we can also make a separate exception differs by type of entity(enum f.e.), but in our case it's not necessary, so i left it with id only.
    private final Long entityId;

    public EntityNotFoundException(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE, entityId);
    }
}
