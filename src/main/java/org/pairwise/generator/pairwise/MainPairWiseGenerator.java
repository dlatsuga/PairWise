package org.pairwise.generator.pairwise;

import org.pairwise.domain.entity.ParameterA;
import org.pairwise.domain.entity.ParameterB;
import org.pairwise.domain.entity.ParameterC;
import org.pairwise.domain.entity.ParameterD;
import org.pairwise.generator.SetDescription;
import org.pairwise.generator.SubSetDescription;
import org.pairwise.generator.pairwise.impl.DefaultPairWiseGenerator;

import java.util.List;
import java.util.Map;

public class MainPairWiseGenerator {
    public static void main(String[] args) {

        SubSetDescription parameter_A = new SubSetDescription("parameter_A", ParameterA.class);
        SubSetDescription parameter_B = new SubSetDescription("parameter_B", ParameterB.class);
        SubSetDescription parameter_C = new SubSetDescription("parameter_C", ParameterC.class);
        SubSetDescription parameter_D = new SubSetDescription("parameter_D", ParameterD.class);

        SetDescription setDescription = new SetDescription();
        setDescription.addSubSet(parameter_A);
        setDescription.addSubSet(parameter_B);
        setDescription.addSubSet(parameter_C);
        setDescription.addSubSet(parameter_D);

        Map<Integer, List<Object>> values = new DefaultPairWiseGenerator(setDescription)
                .generatePairsWiseValues();

        values.forEach((k, v) -> System.out.println(k + ":\t" + v));
    }
}
