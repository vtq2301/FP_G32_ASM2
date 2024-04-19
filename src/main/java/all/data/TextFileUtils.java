package all.data;

import all.models.PolicyHolder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileUtils {

    private static final String FILENAME = "src/main/resources/policy_holders.txt";

    public static void savePolicyHolders(List<PolicyHolder> policyHolders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (PolicyHolder holder : policyHolders) {
                writer.write(holder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<PolicyHolder> loadPolicyHolders() {
        List<PolicyHolder> policyHolders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    PolicyHolder holder = new PolicyHolder(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    policyHolders.add(holder);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return policyHolders;
    }
}
