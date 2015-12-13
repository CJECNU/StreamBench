package fi.aalto.dmg.workloads;

import fi.aalto.dmg.Workload;
import fi.aalto.dmg.exceptions.WorkloadException;
import fi.aalto.dmg.frame.OperatorCreator;
import fi.aalto.dmg.frame.PairWorkloadOperator;
import fi.aalto.dmg.frame.WorkloadOperator;
import fi.aalto.dmg.frame.userfunctions.UserFunctions;
import fi.aalto.dmg.util.WithTime;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by yangjun.wang on 30/10/15.
 * There is no difference between WordCount and WordCountGrouping in Flink
 *
 */
public class WordCountGrouping extends Workload implements Serializable {

    private static final Logger logger = Logger.getLogger(WordCount.class);
    private static final long serialVersionUID = 2835367895719829656L;

    public WordCountGrouping(OperatorCreator creater) throws WorkloadException {
        super(creater);
    }

    private WorkloadOperator<WithTime<String>> kafkaStreamOperator(){
        String topic = this.getProperties().getProperty("topic");
        String groupId = this.getProperties().getProperty("group.id");
        String kafkaServers = this.getProperties().getProperty("bootstrap.servers");
        String zkConnectStr = this.getProperties().getProperty("zookeeper.connect");
        String offset = this.getProperties().getProperty("auto.offset.reset");

        return this.getOperatorCreator().createOperatorFromKafka(zkConnectStr, kafkaServers, groupId, topic, offset);
    }

    public void Process() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            WorkloadOperator<WithTime<String>> operator = kafkaStreamOperator();
            PairWorkloadOperator<String, WithTime<Integer>> counts =
                    operator.flatMap(UserFunctions.splitFlatMapWithTime, "spliter").
                            mapToPair(UserFunctions.mapToStrIntPairWithTime, "pair").
                            groupByKey().reduce(UserFunctions.sumReduceWithTime, "sum").
                            updateStateByKey(UserFunctions.sumReduceWithTime, "cumulate");
            counts.print();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
