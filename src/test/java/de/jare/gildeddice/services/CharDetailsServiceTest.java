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
        profileMock.setCharDetails(null); // Kein CharDetails vorhanden

        User userMock = new User();
        userMock.setProfile(profileMock);

        when(userService.getUser(auth)).thenReturn(userMock);

        // Simuliert das Speichern der neuen CharDetails
        when(charDetailsRepository.save(any(CharDetails.class))).thenAnswer(invocation -> {
            CharDetails savedCharDetails = invocation.getArgument(0);
            savedCharDetails.setId(1L); // ID simulieren
            return savedCharDetails;
        });

        // Act
        CharDetailsResponseDTO responseDTO = charDetailsService.createOrUpdateCharDetails(requestDTO, auth);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(10, responseDTO.intelligence());
        assertEquals(8, responseDTO.negotiate());
        assertEquals(7, responseDTO.ability());
        assertEquals(6, responseDTO.planning());
        assertEquals(5, responseDTO.stamina());
        assertEquals("avatar_url", responseDTO.avatar());

        // Verifiziere die Interaktionen
        verify(userService, times(1)).getUser(auth);
        verify(charDetailsRepository, times(1)).save(any(CharDetails.class));
        verify(userService, times(1)).setUserCharToProfile(any(CharDetails.class), eq(auth));
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
        charDetailsService.setFinancesByChoice(charDetailsId, gamePhase, incomeValue, outcomeValue, oneTimePayment);

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
                charDetailsService.setFinancesByChoice(charDetailsId, gamePhase, null, null, null));
        assertEquals("CharDetails not found!", exception.getMessage());
        verify(charDetailsRepository, never()).save(any());
    }

    @Test
    void testSetFinancesByChoice_WithInvestment() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 9;
        Integer incomeValue = null;
        Integer outcomeValue = null;
        Integer oneTimePayment = null;

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setMoney(1000);
        charDetails.setInvest(1000);
        charDetails.setInvestmentPercent(5);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        charDetailsService.setFinancesByChoice(charDetailsId, gamePhase, incomeValue, outcomeValue, oneTimePayment);

        // Assert
        assertEquals(50, charDetails.getMoney());
        assertEquals(1000, charDetails.getInvest());
        assertEquals(5, charDetails.getInvestmentPercent());

        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetFinancesByChoice_EndOfInvestmentPhase() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 10; // 10er Vielfaches, um Investition zurückzuzahlen
        Integer incomeValue = null;
        Integer outcomeValue = null;
        Integer oneTimePayment = null;

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setMoney(500);
        charDetails.setInvest(1000);
        charDetails.setInvestmentPercent(5);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        charDetailsService.setFinancesByChoice(charDetailsId, gamePhase, incomeValue, outcomeValue, oneTimePayment);

        // Assert
        assertEquals(1500, charDetails.getMoney()); // Investition von 1000 zurückzahlen
        assertEquals(0, charDetails.getInvest());
        assertEquals(0, charDetails.getInvestmentPercent());

        verify(charDetailsRepository, times(1)).save(charDetails);
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

        CharChoices charChoices = new CharChoices();
        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setCharChoices(charChoices);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        charDetailsService.setInventoryByChoice(charDetailsId, study, scholarship, apprenticeship, job, property, rentApartment, car);

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
                charDetailsService.setInventoryByChoice(charDetailsId, true, false, true, true, false, true, false));
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

        CharChoices charChoices = new CharChoices();
        charChoices.setStudy(false);
        charChoices.setScholarship(false);
        charChoices.setApprenticeship(true);
        charChoices.setJob(true);
        charChoices.setProperty(false);
        charChoices.setRentApartment(true);
        charChoices.setCar(false);

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setCharChoices(charChoices);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        charDetailsService.setInventoryByChoice(charDetailsId, study, scholarship, apprenticeship, job, property, rentApartment, car);

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
    void testSetCharacterStatusLvls_Success_NoGameEnd() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 5;
        Integer stressValue = 2;
        Integer satisfactionValue = 1;
        Integer healthValue = -1;

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setStressLvl(3);
        charDetails.setSatisfactionLvl(4);
        charDetails.setHealthLvl(5);
        charDetails.setHandicap(0);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        boolean gameEnd = charDetailsService.setCharacterStatusLvls(charDetailsId, gamePhase, stressValue, satisfactionValue, healthValue);

        // Assert
        assertFalse(gameEnd);
        assertEquals(5, charDetails.getStressLvl()); // 3 + 2
        assertEquals(5, charDetails.getSatisfactionLvl()); // 4 + 1
        assertEquals(4, charDetails.getHealthLvl()); // 5 - 1
        assertEquals(-1, charDetails.getHandicap()); // Satisfaction level increased slightly, reducing handicap by 1
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetCharacterStatusLvls_GameEnd_StressLevel() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 5;
        Integer stressValue = 7; // Stress Level will reach 10

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setStressLvl(3);
        charDetails.setSatisfactionLvl(4);
        charDetails.setHealthLvl(5);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        boolean gameEnd = charDetailsService.setCharacterStatusLvls(charDetailsId, gamePhase, stressValue, null, null);

        // Assert
        assertTrue(gameEnd);
        assertEquals(10, charDetails.getStressLvl());
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetCharacterStatusLvls_GameEnd_HealthLevel() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 5;
        Integer healthValue = -5; // Health Level will reach 0

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setStressLvl(2);
        charDetails.setSatisfactionLvl(4);
        charDetails.setHealthLvl(5);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        boolean gameEnd = charDetailsService.setCharacterStatusLvls(charDetailsId, gamePhase, null, null, healthValue);

        // Assert
        assertTrue(gameEnd);
        assertEquals(0, charDetails.getHealthLvl());
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    @Test
    void testSetCharacterStatusLvls_CharDetailsNotFound() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 5;

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                charDetailsService.setCharacterStatusLvls(charDetailsId, gamePhase, null, null, null));
        assertEquals("CharDetails not found!", exception.getMessage());
        verify(charDetailsRepository, never()).save(any());
    }

    @Test
    void testSetCharacterStatusLvls_HandicapAdjustment() {
        // Arrange
        long charDetailsId = 1L;
        int gamePhase = 10;
        Integer stressValue = 4; // Brings stress level to 8, triggers handicap and potential health decrease

        CharDetails charDetails = new CharDetails();
        charDetails.setId(charDetailsId);
        charDetails.setStressLvl(4);
        charDetails.setSatisfactionLvl(5);
        charDetails.setHealthLvl(5);
        charDetails.setHandicap(0);

        when(charDetailsRepository.findById(charDetailsId)).thenReturn(Optional.of(charDetails));

        // Act
        boolean gameEnd = charDetailsService.setCharacterStatusLvls(charDetailsId, gamePhase, stressValue, null, null);

        // Assert
        assertFalse(gameEnd);
        assertEquals(8, charDetails.getStressLvl());
        assertEquals(4, charDetails.getHealthLvl()); // Health decreases by 1 as gamePhase % 10 == 0
        assertEquals(-2, charDetails.getHandicap()); // Handicap adjustment based on stress level
        verify(charDetailsRepository, times(1)).save(charDetails);
    }

    private CharDetails createCharDetails(long id, int stressLvl, int satisfactionLvl, int healthLvl) {
        CharDetails charDetails = new CharDetails();
        charDetails.setId(id);
        charDetails.setStressLvl(stressLvl);
        charDetails.setSatisfactionLvl(satisfactionLvl);
        charDetails.setHealthLvl(healthLvl);
        return charDetails;
    }



    @Test
    void testResetChar_Success() {
        // Arrange
        Authentication auth = mock(Authentication.class);

        CharChoices initialCharChoices = new CharChoices();
        CharChoices resetCharChoices = new CharChoices();

        CharDetails charDetails = new CharDetails();
        charDetails.setStressLvl(7);
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
        userProfile.setCharDetails(charDetails);

        when(userService.getUserProfile(auth)).thenReturn(userProfile);

        // Act
        charDetailsService.resetChar(auth);

        // Assert
        assertEquals(0, charDetails.getStressLvl());
        assertEquals(5, charDetails.getSatisfactionLvl());
        assertEquals(10, charDetails.getHealthLvl());
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