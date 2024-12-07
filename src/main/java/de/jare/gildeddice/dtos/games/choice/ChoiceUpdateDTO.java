package de.jare.gildeddice.dtos.games.choice;

public record ChoiceUpdateDTO(
        long id,
        String title,
        String skill,
        int minDiceValue,
        String startMessage,

        String winMessage,
        Integer winIncomeValue,
        Integer winOutcomeValue,
        Float winInvestmentPercent,
        Integer winOneTimePayment,
        Boolean winStudy,
        Boolean winScholarship,
        Boolean winApprenticeship,
        Boolean winJob,
        Boolean winProperty,
        Boolean winRentApartment,
        Boolean winCar,
        Integer winStressValue,
        Integer winSatisfactionValue,
        Integer winHealthValue,

        String loseMessage,
        Integer loseIncomeValue,
        Integer loseOutcomeValue,
        Float loseInvestmentPercent,
        Integer loseOneTimePayment,
        Boolean loseStudy,
        Boolean loseScholarship,
        Boolean loseApprenticeship,
        Boolean loseJob,
        Boolean loseProperty,
        Boolean loseRentApartment,
        Boolean loseCar,
        Integer loseStressValue,
        Integer loseSatisfactionValue,
        Integer loseHealthValue,


        String critMessage,
        Integer critIncomeValue,
        Integer critOutcomeValue,
        Float critInvestmentPercent,
        Integer critOneTimePayment,
        Boolean critScholarship,
        Integer critStressValue,
        Integer critSatisfactionValue,
        Integer critHealthValue,


        long npcId
) {
}
