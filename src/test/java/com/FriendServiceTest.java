package com;

import com.skysoft.dto.request.DeleteFriendRequest;
import com.skysoft.dto.response.GetAllFriendsResponse;
import com.skysoft.model.AccountEntity;
import com.skysoft.model.FriendEntity;
import com.skysoft.service.FriendDBService;
import com.skysoft.service.impl.FriendServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.gen5.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Collections;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FriendServiceTest {

    @Mock
    private FriendDBService friendDBService;

    @Mock
    private ModelMapper mapper;

    @Spy
    @InjectMocks
    private FriendServiceImpl friendService;

    private FriendEntity friendEntity;
    private AccountEntity accountEntity = new AccountEntity();
    private AccountEntity accountEntity2 = new AccountEntity();

    @Before
    public void setUp() {
        accountEntity.setUsername("bob");
        accountEntity2.setUsername("pop");
        friendEntity = new FriendEntity(accountEntity, accountEntity2);
    }

    @Test
    public void getAllFriendsTest() {
        when(friendDBService.getAllFriendsByUsernameAndStatus(anyString(), any())).thenReturn(Collections.singletonList(friendEntity));

        GetAllFriendsResponse response = friendService.getAllFriends(accountEntity.getUsername());
        assertEquals(1, response.getPersonalDetails().size());

        verify(friendService, times(1)).getAllFriends(accountEntity.getUsername());
    }

    @Test
    public void deleteFriendTest() {
        String currentUser = "pop";
        when(friendDBService.getActiveByUsernameAndFriendName("bob", "pop")).thenReturn(friendEntity);

        friendService.deleteFriend("bob", currentUser);

        verify(friendService, times(1)).deleteFriend("bob", currentUser);
    }

    @Test
    public void failDeleteFriendTest(){
        Assertions.assertThrows(Exception.class, () -> friendService.deleteFriend(any(), any()));
    }
}
