package com.RPGServer.EncounterSystem;

import com.RPGServer.ItemSystem.Item;

import java.util.ArrayList;
import java.util.Map;

public class RewardStep extends EncounterStep
{
    public RewardStep()
    {
        this.stepType = 3;
    }

    @Override
    public void endStep(int selectedChoice)
    {

    }

    @Override
    public StepUpdate postStepUpdate(StepUpdate update)
    {

        RewardUpdate output = new RewardUpdate(parentEncounter.encounterRewards);
        //Fill with relevant information about reward
        return output;
    }

    @Override
    public StepUpdate getInitialStepUpdate()
    {
        return postStepUpdate(null);
    }

    public class RewardUpdate extends StepUpdate
    {
        int goldReward;
        int experienceReward;
        ArrayList<String> itemList;

        public RewardUpdate(ArrayList<Encounter.Reward> rewardList)
        {
            this.stepType = 3;
            goldReward = 0;
            experienceReward = 0;
            itemList = new ArrayList<String>();
            //Add each item from the rewards list
            for(int i = 0; i < rewardList.size(); i++)
            {
                goldReward += rewardList.get(i).goldReward;
                experienceReward += rewardList.get(i).experienceReward;
                ArrayList<Item> itemObjects = rewardList.get(i).itemRewards;
                for(int j = 0; j < itemObjects.size(); j++)
                {
                    itemList.add(itemObjects.get(j).name);
                }
            }
        }
        @Override
        public Map<String, Object> toMap()
        {
            Map<String, Object> output = super.toMap();
            output.put("goldReward", goldReward);
            output.put("experienceReward", experienceReward);
            output.put("itemList", itemList);

            return output;
        }
    }
}
