package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.characters.CharDetailsRequestDTO;
import de.jare.gildeddice.dtos.characters.CharDetailsResponseDTO;
import de.jare.gildeddice.dtos.characters.MoneyResponseDTO;
import de.jare.gildeddice.entities.enums.Category;
import de.jare.gildeddice.entities.games.storys.PlusStory;
import de.jare.gildeddice.entities.users.character.CharChoices;
import de.jare.gildeddice.entities.users.character.CharDetails;
import de.jare.gildeddice.entities.users.Profile;
import de.jare.gildeddice.entities.users.User;
import de.jare.gildeddice.repositories.CharDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CharDetailsServiceTest {

    @Mock
    private CharDetailsRepository charDetailsRepository;

    @Mock
    private UserService userService;

    @Mock
    private PlusStoryService plusStoryService;

    @InjectMocks
    private CharDetailsService charDetailsService;

    public CharDetailsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCharDetails_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        CharDetails charDetails = new CharDetails();
        charDetails.setIntelligence(10);
        charDetails.setNegotiate(8);
        charDetails.setCharChoices(new CharChoices());

        Profile profileMock = new Profile();
        profileMock.setCharDetails(charDetails);

        when(userService.getUserProfile(auth)).thenReturn(profileMock);

        // Act
        CharDetailsResponseDTO responseDTO = charDetailsService.getCharDetails(auth);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(10, responseDTO.intelligence());
        assertEquals(8, responseDTO.negotiate());

        verify(userService, times(1)).getUserProfile(auth);
    }

    @Test
    void testCreateOrUpdateCharDetails_NewCharacter_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        CharDetailsRequestDTO requestDTO = new CharDetailsRequestDTO(10, 8, 7, 6, 5, "avatar_url");

        Profile profileMock = new Profile();
        profileMock.setUsername("username");
        profileMock.setCharDetails(null); // Noch kein CharDetails

        User userMock = new User();
        userMock.setId(1L);
        userMock.setProfile(profileMock);

        // Wir mocken, dass der userService diesen User zurückgibt
        when(userService.getUser(auth)).thenReturn(userMock);

        // Speichern von CharDetails soll eine ID setzen
        when(charDetailsRepository.save(any(CharDetails.class))).thenAnswer(invocation -> {
            CharDetails savedCharDetails = invocation.getArgument(0);
            savedCharDetails.setId(1L); // ID simulieren
            return savedCharDetails;
        });

        // Act
        CharDetailsResponseDTO response = charDetailsService.createOrUpdateCharDetails(requestDTO, auth);

        // Assert
        // Jetzt sollte im userMock.getProfile() ein CharDetails Objekt vorhanden sein.
        assertNotNull(userMock.getProfile().getCharDetails(), "CharDetails sollte nach dem Erstellen nicht mehr null sein");
        assertEquals(1L, userMock.getProfile().getCharDetails().getId(), "CharDetails ID sollte gesetzt sein");
        assertEquals(10, userMock.getProfile().getCharDetails().getIntelligence());
        assertEquals(8, userMock.getProfile().getCharDetails().getNegotiate());
        assertEquals(7, userMock.getProfile().getCharDetails().getAbility());
        assertEquals(6, userMock.getProfile().getCharDetails().getPlanning());
        assertEquals(5, userMock.getProfile().getCharDetails().getStamina());
        assertEquals("avatar_url", userMock.getProfile().getCharDetails().getAvatar());

        // Verifizieren, dass setUserCharToProfile aufgerufen wurde
        verify(userService, times(1)).setUserCharToProfile(any(CharDetails.class), eq(auth));
        verify(charDetailsRepository, times(1)).save(any(CharDetails.class));
    }



    @Test
    void testCreateOrUpdateCharDetails_UpdateCharacter_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        CharDetailsRequestDTO requestDTO = new CharDetailsRequestDTO(10, 8, 7, 6, 5, "avatar_url");

        CharDetails existingCharDetails = new CharDetails();
        existingCharDetails.setIntelligence(5);
        existingCharDetails.setCharChoices(new CharChoices());

        Profile profileMock = new Profile();
        profileMock.setCharDetails(existingCharDetails);

        User userMock = new User();
        userMock.setProfile(profileMock);

        when(userService.getUser(auth)).thenReturn(userMock);
        when(charDetailsRepository.save(existingCharDetails)).thenReturn(existingCharDetails);

        // Act
        CharDetailsResponseDTO responseDTO = charDetailsService.createOrUpdateCharDetails(requestDTO, auth);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(10, responseDTO.intelligence());
        assertEquals(8, responseDTO.negotiate());

        verify(userService, times(1)).getUser(auth);
        verify(charDetailsRepository, times(1)).save(existingCharDetails);
        verify(userService, times(1)).setUserCharToProfile(existingCharDetails, auth);
    }

    @Test
    void testGetUserAvatar_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        String avatarUrl = "avatar_url";
        Profile profileMock = new Profile();
        CharDetails charDetailsMock = new CharDetails();
        charDetailsMock.setId(1L);
        charDetailsMock.setAvatar(avatarUrl);
        profileMock.setId(1L);
        profileMock.setCharDetails(charDetailsMock);


        when(userService.getUserProfile(auth)).thenReturn(profileMock);

        // Act
        String actualAvatarUrl = charDetailsService.getUserAvatar(auth);

        // Assert
        assertEquals(avatarUrl, actualAvatarUrl);

        verify(userService, times(1)).getUserProfile(auth);
    }

    @Test
    void testGetUserAvatar_noAvatar_Url() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        Profile profileMock = new Profile();
        CharDetails charDetailsMock = new CharDetails();
        charDetailsMock.setId(1L);
        profileMock.setId(1L);
        profileMock.setCharDetails(charDetailsMock);

        when(userService.getUserProfile(auth)).thenReturn(profileMock);

        // Act
        String actualAvatarUrl = charDetailsService.getUserAvatar(auth);

        // Assert
        assertNull(actualAvatarUrl);
        verify(userService, times(1)).getUserProfile(auth);
    }


    @Test
    void testGetAllFinancial_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);

        CharDetails charDetailsMock = new CharDetails();
        charDetailsMock.setId(1L);
        charDetailsMock.setIncome(1000);
        charDetailsMock.setOutcome(500);
        charDetailsMock.setInvest(200);
        charDetailsMock.setMoney(50000);

        Profile profileMock = new Profile();
        profileMock.setId(1L);
        profileMock.setCharDetails(charDetailsMock);

        when(userService.getUserProfile(auth)).thenReturn(profileMock);

        // Act
        MoneyResponseDTO actualMoneyResponseDTO = charDetailsService.getAllFinancial(auth);

        // Assert
        assertNotNull(actualMoneyResponseDTO);
        assertEquals(1000, actualMoneyResponseDTO.income());
        assertEquals(500, actualMoneyResponseDTO.outcome());
        assertEquals(200, actualMoneyResponseDTO.invest());
        assertEquals(50000, actualMoneyResponseDTO.money());
    }




    @Test
    void testSetFinancesByChoice_Success() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 5;
        Integer incomeValue = 500;
        Integer outcomeValue = 100;
        Integer oneTimePayment = 50;

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setMoney(1000);
        charDetails.setOutcome(200);
        charDetails.setInvest(0);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        charDetailsService.setFinancesByChoice(charDetailsId, incomeValue, outcomeValue, oneTimePayment);

        // Assert
        assertEquals(incomeValue, charDetails.getIncome());
        assertEquals(300, charDetails.getOutcome());
        assertEquals(1050, charDetails.getMoney());

        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetFinancesByChoice_CharDetailsNotFound() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 5;

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                charDetailsService.setFinancesByChoice(charDetailsId, null, null, null));
        assertEquals("CharDetails not found!", exception.getMessage());
        verify(charDetailsRepository, never()).save(any());
    }




    @Test
    void testSetInventoryByChoice_Success() {
        // Arrange
        long charDetailsId = 1L;
        Boolean study = true;
        Boolean scholarship = false;
        Boolean apprenticeship = true;
        Boolean job = true;
        Boolean property = false;
        Boolean rentApartment = true;
        Boolean car = false;
        Boolean driverLicense = false;

        CharChoices charChoices = new CharChoices();
        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setCharChoices(charChoices);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        charDetailsService.setInventoryByChoice(charDetailsId, study, scholarship, apprenticeship, job, property, rentApartment, car, driverLicense);

        // Assert
        assertTrue(charChoices.isStudy());
        assertFalse(charChoices.isScholarship());
        assertTrue(charChoices.isApprenticeship());
        assertTrue(charChoices.isJob());
        assertFalse(charChoices.isProperty());
        assertTrue(charChoices.isRentApartment());
        assertFalse(charChoices.isCar());

        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetInventoryByChoice_CharDetailsNotFound() {
        // Arrange
        long charDetailsId = 1L;

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                charDetailsService.setInventoryByChoice(charDetailsId, true, false, true, true, false, true, false, false));
        assertEquals("CharDetails not found!", exception.getMessage());
        verify(charDetailsRepository, never()).save(any());
    }

    @Test
    void testSetInventoryByChoice_PartialUpdates() {
        // Arrange
        long charDetailsId = 1L;
        Boolean study = null; // Keine Änderung
        Boolean scholarship = true;
        Boolean apprenticeship = null; // Keine Änderung
        Boolean job = false;
        Boolean property = true;
        Boolean rentApartment = null; // Keine Änderung
        Boolean car = true;
        Boolean driverLicense = true;

        CharChoices charChoices = new CharChoices();
        charChoices.setStudy(false);
        charChoices.setScholarship(false);
        charChoices.setApprenticeship(true);
        charChoices.setJob(true);
        charChoices.setProperty(false);
        charChoices.setRentApartment(true);
        charChoices.setCar(false);
        charChoices.setDriverLicense(false);

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setCharChoices(charChoices);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        charDetailsService.setInventoryByChoice(charDetailsId, study, scholarship, apprenticeship, job, property, rentApartment, car, driverLicense);

        // Assert
        assertFalse(charChoices.isStudy()); // Unverändert
        assertTrue(charChoices.isScholarship());
        assertTrue(charChoices.isApprenticeship()); // Unverändert
        assertFalse(charChoices.isJob());
        assertTrue(charChoices.isProperty());
        assertTrue(charChoices.isRentApartment()); // Unverändert
        assertTrue(charChoices.isCar());

        verify(charDetailsRepository, times(1)).save(charDetails);
    }




    @Test
    void testSetCharacterStatusLvls_CharDetailsNotFound() {
        // Arrange
        long id = 1L;
        when(charDetailsRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                charDetailsService.setCharacterStatusLvls(id, 1, 5, 3, 2));
        assertEquals("CharDetails not found!", exception.getMessage());
        verify(charDetailsRepository, times(1)).findById(id);
    }

    @Test
    void testSetCharacterStatusLvls_StressLevelGameEnd() {
        // Arrange
        long id = 1L;
        CharDetails charDetails = new CharDetails();
        charDetails.setStressLvl(9); // Start bei 9
        when(charDetailsRepository.findById(id)).thenReturn(Optional.of(charDetails));

        // Act
        boolean result = charDetailsService.setCharacterStatusLvls(id, 1, 1, null, null); // Stress +1

        // Assert
        assertTrue(result);
        assertEquals(10, charDetails.getStressLvl());
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetCharacterStatusLvls_HealthLevelGameEnd() {
        // Arrange
        long id = 1L;
        CharDetails charDetails = new CharDetails();
        charDetails.setHealthLvl(1); // Start bei 1
        when(charDetailsRepository.findById(id)).thenReturn(Optional.of(charDetails));

        // Act
        boolean result = charDetailsService.setCharacterStatusLvls(id, 1, null, null, -1); // Gesundheit -1

        // Assert
        assertTrue(result);
        assertEquals(0, charDetails.getHealthLvl());
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetCharacterStatusLvls_NormalAdjustments() {
        // Arrange
        long id = 1L;
        CharDetails charDetails = new CharDetails();
        charDetails.setStressLvl(5);
        charDetails.setSatisfactionLvl(3);
        charDetails.setHealthLvl(10);
        when(charDetailsRepository.findById(id)).thenReturn(Optional.of(charDetails));

        // Act
        boolean result = charDetailsService.setCharacterStatusLvls(id, 1, 1, 2, -2);

        // Assert
        assertFalse(result);
        assertEquals(6, charDetails.getStressLvl()); // Stress +1
        assertEquals(5, charDetails.getSatisfactionLvl()); // Satisfaction +2
        assertEquals(8, charDetails.getHealthLvl()); // Health -2
        assertEquals(-1, charDetails.getHandicap()); // Handicap angepasst
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetCharacterStatusLvls_OverMinMaxAdjustments() {
        // Arrange
        long id = 1L;
        CharDetails charDetails = new CharDetails();
        charDetails.setStressLvl(0);
        charDetails.setSatisfactionLvl(4);
        charDetails.setHealthLvl(19);
        when(charDetailsRepository.findById(id)).thenReturn(Optional.of(charDetails));

        // Act
        boolean result = charDetailsService.setCharacterStatusLvls(id, 11, -1, 1, 2);

        // Assert
        assertFalse(result);
        assertEquals(0, charDetails.getStressLvl()); // Stress over min = 0
        assertEquals(5, charDetails.getSatisfactionLvl()); // Satisfaction +1
        assertEquals(20, charDetails.getHealthLvl()); // Health over max = 20
        assertEquals(0, charDetails.getHandicap()); 
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetCharacterStatusLvls_StressAndSatisfactionBoundary() {
        // Arrange
        long id = 1L;
        CharDetails charDetails = new CharDetails();
        charDetails.setStressLvl(7);
        charDetails.setSatisfactionLvl(3);
        charDetails.setHealthLvl(10);
        when(charDetailsRepository.findById(id)).thenReturn(Optional.of(charDetails));

        // Act
        boolean result = charDetailsService.setCharacterStatusLvls(id, 10, 1, -1, -1);

        // Assert
        assertFalse(result);
        assertEquals(8, charDetails.getStressLvl()); // Stress +1
        assertEquals(2, charDetails.getSatisfactionLvl()); // Satisfaction +1
        assertEquals(7, charDetails.getHealthLvl()); // Health -1
        assertEquals(-4, charDetails.getHandicap()); // Handicap angepasst
        verify(charDetailsRepository, times(1)).save(charDetails);
    }


    @Test
    void testResetChar_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        CharChoices initialCharChoices = new CharChoices();
        initialCharChoices.setStudy(true);
        CharChoices resetCharChoices = new CharChoices();

        CharDetails charDetails = new CharDetails();
        charDetails.setId(1L);
        charDetails.setStressLvl(10);
        charDetails.setSatisfactionLvl(3);
        charDetails.setHealthLvl(4);
        charDetails.setHandicap(-2);
        charDetails.setIncome(1000);
        charDetails.setOutcome(200);
        charDetails.setInvest(500);
        charDetails.setInvestmentPercent(10);
        charDetails.setMoney(1500);
        charDetails.setCharChoices(initialCharChoices);

        Profile userProfile = new Profile();
        userProfile.setId(1L);
        userProfile.setCharDetails(charDetails);

        when(userService.getUserProfile(auth)).thenReturn(userProfile);

        // Act
        charDetailsService.resetChar(auth);

        // Assert
        assertEquals(0, charDetails.getStressLvl());
        assertEquals(5, charDetails.getSatisfactionLvl());
        assertEquals(20, charDetails.getHealthLvl());
        assertEquals(0, charDetails.getHandicap());
        assertEquals(0, charDetails.getIncome());
        assertEquals(0, charDetails.getOutcome());
        assertEquals(0, charDetails.getInvest());
        assertEquals(0, charDetails.getInvestmentPercent());
        assertEquals(0, charDetails.getMoney());

        assertNotNull(charDetails.getCharChoices());
        assertEquals(resetCharChoices, charDetails.getCharChoices()); // Vergleiche mit einem neuen CharChoices-Objekt

        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testResetChar_CharDetailsNotFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        Profile userProfile = new Profile();
        userProfile.setCharDetails(null); // CharDetails ist null

        when(userService.getUserProfile(auth)).thenReturn(userProfile);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> charDetailsService.resetChar(auth));
        verify(charDetailsRepository, never()).save(any());
    }

    @Test
    void testResetChar_NoAuthentication() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(userService.getUserProfile(auth)).thenThrow(new EntityNotFoundException("User profile not found"));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                charDetailsService.resetChar(auth));
        assertEquals("User profile not found", exception.getMessage());
        verify(charDetailsRepository, never()).save(any());
    }

    //---------------


    @Test
    void testSetInvesting_Success() {
        // Arrange
        long plusStoryId = 1L;
        int incomeValue = 500;
        Authentication auth = mock(Authentication.class);

        PlusStory plusStory = new PlusStory();
        plusStory.setCategory(Category.INVESTMENT);
        when(plusStoryService.getPlusStory(plusStoryId)).thenReturn(plusStory);

        Profile profile = new Profile();
        CharDetails charDetails = new CharDetails();
        profile.setCharDetails(charDetails);
        when(userService.getUserProfile(auth)).thenReturn(profile);

        // Act
        charDetailsService.setInvesting(plusStoryId, incomeValue, auth);

        // Assert
        assertEquals(incomeValue, charDetails.getInvest());
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetInvesting_WrongCategory() {
        // Arrange
        Profile profile = new Profile();
        CharDetails charDetails = new CharDetails();
        profile.setCharDetails(charDetails);

        long plusStoryId = 1L;
        PlusStory plusStory = new PlusStory();
        plusStory.setId(plusStoryId);
        plusStory.setCategory(Category.FATE); // Wrong category

        int incomeValue = 500;
        Authentication auth = mock(Authentication.class);

        when(userService.getUserProfile(auth)).thenReturn(profile);
        when(plusStoryService.getPlusStory(plusStoryId)).thenReturn(plusStory);

        // Act & Assert
        WrongThreadException exception = assertThrows(WrongThreadException.class, () ->
                charDetailsService.setInvesting(plusStoryId, incomeValue, auth));
        assertEquals("PlusStory is not an Inventory Story", exception.getMessage());
        verify(charDetailsRepository, never()).save(any());
    }

    @Test
    void testSetInvesting_NegativeIncomeValue() {
        // Arrange
        long plusStoryId = 1L;
        int incomeValue = -100; // Negative value
        Authentication auth = mock(Authentication.class);

        PlusStory plusStory = new PlusStory();
        plusStory.setCategory(Category.INVESTMENT);
        when(plusStoryService.getPlusStory(plusStoryId)).thenReturn(plusStory);

        Profile profile = new Profile();
        CharDetails charDetails = new CharDetails();
        profile.setCharDetails(charDetails);
        when(userService.getUserProfile(auth)).thenReturn(profile);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                charDetailsService.setInvesting(plusStoryId, incomeValue, auth));
        assertEquals("Investment value negative or null", exception.getMessage());
        verify(charDetailsRepository, never()).save(any());
    }





}