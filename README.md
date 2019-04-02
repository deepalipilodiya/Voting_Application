# Voting_Application
Success Party Venue Section Voting Application

Your team is going to celebrate their successful software release with an outing. To keep things as
fair as possible they have agreed to vote on their choice of outing, and use a preferential vote
counting scheme. Write an application that helps people enter their votes on to a virtual ballot
paper, then counts the votes to select a winner.

How To Run The App :

This App expects three Inputs from Command Line Arguments to Start the Application :
1. File which will have Venue Destinations. Each Record will be one Venue.
2. Maximum Number of Prefrences each voter can give. If value 3 is passed, it means they can select three prefrences out of the provided venue options.
3. Number of Voter who are going to Participate in the Voting


After Starting the Application, Application will need Console Input from all the Voters who are participating ,These will be there preference and will be considered as one Ballot. Venue selected first will be the First Preference, Second Valuse selected will be the Second Prefrence and So on. If some voter wants to give no more preference ,just press 0. It will end your ballot and ask new Voter to enter there Inputs.

Console Input will be the numbers displayed against each Venue Name. For every voter the List will be dispalyed on screen before entering Input. Select values from the List. Press 0 to exit if you dont want to give anymore prefrence.

Run Command Template :

Application_Jar_Name <File_Location> <Max_No_Of_Prefrences(Integer)> <No_Of_Voters(Integer)>


