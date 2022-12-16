package com.example;

import java.util.ArrayList;

public class Main {

    /**
     * Handles an individual file. Unions Together opposing groups
     * And outputs both the attacks, and the group counts
     * @param file file to read and parse
     */
    public static void handleFile(String file) {
        System.out.println(file);

        InputData data = new InputData("data/" + file);
        UnionFind set = new UnionFind(data.numberOfVoters);

        for (int[] attack: data.attacks) {
            InputData.Member attacker = data.members.get(attack[0] - 1);
            InputData.Member attacked = data.members.get(attack[1] - 1);;

            String output = (attacker.value + 1) + " ⚔ Attacks " + (attacked.value + 1);

            if (set.inSameGroup(attacker.value, attacked.value)) {
                output = (attacker.value + 1) + " ⚔ Attacks(Ignored) " + (attacked.value + 1);
            } else {
                handleAttack(attacker, attacked, set);
            }
            System.out.println(output);
        }

        ArrayList<Integer> groups = set.getGroupRoots();
        groups.forEach((group) -> {
            System.out.println("Group " + (group + 1) + " has " + set.groupSize(group) + " members");
        });
    }

    /**
     * Handles whether or not to set the previousOpponent on each attacker/attacked value
     * @param attackerMember the value that is attacking
     * @param attackedMember the value that is being attacked
     * @param set UnionFind set of all values
     */
    public static void handleAttack(InputData.Member attackerMember, InputData.Member attackedMember, UnionFind set) {

        // Check if we need to union
        if (attackerMember.previousOpponent != -1)
            set.union(attackedMember.value, attackerMember.previousOpponent);
        if (attackedMember.previousOpponent != -1)
            set.union(attackerMember.value, attackedMember.previousOpponent);

        attackerMember.previousOpponent = attackedMember.value;
        attackedMember.previousOpponent = attackerMember.value;
    }

    public static void main(String[] args) {
	    String[] files = {"case1.txt", "case2.txt", "case3.txt", "case4.txt",
                          "case5.txt", "case6.txt", "case7.txt"};
//        String[] files = {"case1.txt"};

	    for (String file: files) {
            handleFile(file);
        }
    }
}
