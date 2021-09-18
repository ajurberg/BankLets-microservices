package br.com.letscode.eventservice.event;

import br.com.letscode.eventservice.user.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

class ConverterEventDTO {

    private ConverterEventDTO() {
        super();
    }

    static Event parseToEventMono(EventDTO eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
                .userId(eventDTO.getUser().getUserId())
                .accountIdList(EventDTO.convertAccountDTOListToAccountIdList(eventDTO.getAccountList()))
                .build();
    }

    static List<Event> parseToEventList(List<EventDTO> eventDTOListMock) {
        return eventDTOListMock.stream()
                .map(ConverterEventDTO::parseToEventMono)
                .collect(Collectors.toList());
    }

    static EventDTO of(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .user(UserDTO.builder().userId(event.getId())
                        .build())
                .accountList(EventDTO.convertAccountIdListToAccountDTOList(event
                        .getAccountIdList())).build();
    }

}
