package de.jare.gildeddice.dtos.games;

public record ChoiceCreateDTO(
        String title,
        String skill,
        int minDiceValue,
        String startMessage,

        String winMessage,
        Integer winIncomeValue,
        Integer winOutcomeValue,
        Integer winInvestmentPercent,
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
        Integer loseInvestmentPercent,
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
        Integer critInvestmentPercent,
        Integer critOneTimePayment,
        Boolean critScholarship,
        Integer critStressValue,
        Integer critSatisfactionValue,
        Integer critHealthValue,


        long npcId
) {
}
