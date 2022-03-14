# Kafka

> 链接：https://juejin.cn/post/6844903919328428040

## kafka是什么

kafka在设计之初的时候 开发人员们在除了消息中间件以外，还想把kafka设计为一个能够存储数据的系统，有点像常见的非关系型数据库，比如说NoSql等。除此之外  还希望kafka能支持持续变化，不断增长的数据流,  可以发布 和订阅数据流，还可以对于这些数据进行保存。

也就是说kafka的本质 是一个数据存储平台，流平台 ， 只是他在做消息发布，消息消费的时候我们可以把他当做消息中间件来用。

而且kafka在设计之初就是采用分布式架构设计的， 基于集群的方式工作，且可以自由伸缩，所以 kafka构建集群非常简单。

## 基本概念

- Broker : 和AMQP里协议的概念一样， 就是消息中间件所在的服务器。
- Topic(主题) : 每条发布到Kafka集群的消息都有一个类别，这个类别被称为Topic。（物理上不同Topic的消息分开存储，逻辑上一个Topic的消息虽然保存于一个或多个broker上但用户只需指定消息的Topic即可生产或消费数据而不必关心数据存于何处）。
- Partition(分区) : Partition是物理上的概念，体现在磁盘上面，每个Topic包含一个或多个Partition。
- Producer : 负责发布消息到Kafka broker。
- Consumer : 消息消费者，向Kafka broker读取消息的客户端。

- Consumer Group（消费者群组） : 每个Consumer属于一个特定的Consumer Group（可为每个Consumer指定group name，若不指定group name则属于默认的group）。
- offset 偏移量： 是kafka用来确定消息是否被消费过的标识，在kafka内部体现就是一个递增的数字

kafka消息发送的时候 ,考虑到性能 可以采用打包方式发送， 也就是说 传统的消息是一条一条发送， 现在可以先把需要发送的消息缓存在客户端， 等到达一定数值时， 再一起打包发送， 而且还可以对发送的数据进行压缩处理，减少在数据传输时的开销。

## kafka优缺点

优点：

- 基于磁盘的数据存储
- 高伸缩性
- 高性能
- 应用场景：收集指标和日志 提交日志 流处理

缺点：

- 运维难度大
- 偶尔有数据混乱的情况
- 对zookeeper强依赖
- 多副本模式下对带宽有一定要求

## Kafka基础架构

1. Message：消息是Kafka中最基本的数据单元。消息由一串字节构成，其中主要由key和value构成，key和value也都是byte数组。key的主要作用是根据一定的策略，将此消息路由到指定的分区中，这样就可以保证包含同一key的消息全部写入同一分区中，key可以是null。消息的真正有效负载是value部分的数据。为了提高网络和存储的利用率，生产者会批量发送消息到Kafka，并在发送之前对消息进行压缩。

2. Producer：消息生产者，就是向 kafka broker 发消息的客户端；

3. Consumer：消息消费者，向 kafka broker 取消息的客户端；

4. Consumer Group(CG)：消费者组，由多个 consumer 组成。消费者组内每个消费者负责消费不同分区的数据，一个分区只能由一个组内消费者消费；消费者组之间互不影响。所有的消费者都属于某个消费者组，即消费者组是逻辑上的一个订阅者。

5. Broker：一台 kafka 服务器就是一个 broker。一个集群由多个 broker 组成。一个 broker可以容纳多个topic。

6. Topic：可以理解为一个队列，生产者和消费者面向的都是一个 topic；

7. Partition：为了实现扩展性，一个非常大的 topic 可以分布到多个broker（即服务器）上，一个topic 可以分为多个 partition，每个 partition 是一个有序的队列；

   partition在逻辑上对应一个log。当生产者将消息写入分区时，实际上是写入到了分区对应的Log中。Log是一个逻辑概念，可以对应到磁盘上的一个文件夹。Log由多个Segment组成，每个Segment对应一个日志文件(.log)和索引文件(.index)。

   为避免出现超大文件，每个日志文件的大小是有限制的，当超出 限制后则会创建新的Segment，继续对外提供服务。这里要注意，因为Kafka采用顺序I/O，所以只向最新的Segment追加数据。

   Kafka的message是按topic分类存储的，topic中的数据又是按照一个一个的partition即分区存储到不同broker节点。每个partition对应了操作系统上的一个文件夹，partition实际上又是按照segment分段存储的。这也非常符合分布式系统分区分桶的设计思想。

   通过这种分区分段的设计，Kafka的message消息实际上是分布式存储在一个一个小的segment中的，每次文件操作也是直接操作的segment。为了进一步的查询优化，Kafka又默认为分段后的数据文件建立了索引文件，就是文件系统上的.index文件。这种分区分段+索引的设计，不仅提升了数据读取的效率，同时也提高了数据操作的并行度。

8. Replica：副本，为保证集群中的某个节点发生故障时，该节点上的 partition 数据不丢失，且kafka 仍然能够继续工作，kafka 提供了副本机制，一个 topic 的每个分区都有若干个副本，一个leader 和若干个 follower。

9. leader：每个分区多个副本的“主”，生产者发送数据的对象，以及消费者消费数据的对象都是leader。

10. follower：每个分区多个副本中的“从”，实时从 leader 中同步数据，保持和 leader 数据的同步。leader 发生故障时，某个 follower 会成为新的 follower。

## kafka启动

Linux: bin\kafka-server-start.sh config\server.properties

Windows: bin\windows\kafka-server-start.bat config\server.properties

## server参数解释

log.dirs:  日志文件存储地址， 可以设置多个

num.recovery.threads.per.data.dir：用来读取日志文件的线程数量，对应每一个log.dirs  若此参数为2  log.dirs 为2个目录 那么就会有4个线程来读取

auto.create.topics.enable:是否自动创建tiopic

num.partitions: 创建topic的时候自动创建多少个分区 (可以在创建topic的时候手动指定)

log.retention.hours: 日志文件保留时间  超时即删除

log.retention.bytes: 日志文件最大大小

log.segment.bytes: 当日志文件达到一定大小时，开辟新的文件来存储(分片存储)

log.segment.ms: 同上 只是当达到一定时间时 开辟新的文件

message.max.bytes: broker能接收的最大消息大小(单条) 默认1M

## kafka基本管理操作命令

- 列出所有主题 
  kafka-topics.bat --zookeeper localhost:2181/kafka --list
- 列出所有主题的详细信息
  kafka-topics.bat --zookeeper localhost:2181/kafka --describe
- 创建主题 主题名 my-topic，1副本，8分区
  kafka-topics.bat --zookeeper localhost:2181/kafka --create --replication-factor 1 --partitions 8 --topic my-topic
- 增加分区（分区无法被删除）
  kafka-topics.bat --zookeeper localhost:2181/kafka --alter --topic my-topic --partitions 16
- 删除主题
  kafka-topics.bat --zookeeper localhost:2181/kafka --delete --topic my-topic
- 列出消费者群组（仅Linux ）
  kafka-topics.sh --new-consumer --bootstrape-server localhost:9092/kafka --list
- 列出消费者群组详细信息（仅Linux）
  kafka-topics.sh --new-consimer --bootstrape-server localhost:9092/kafka --describe --group 群组名

## Kafka工作流程

Kafka 中消息是以 **topic** 进行分类的，生产者生产消息，消费者消费消息，都是面向 topic的。topic 是逻辑上的概念，而 partition 是物理上的概念，每个 partition 对应于一个 log 文件，该 log 文件中存储的就是 producer 生产的数据。Producer 生产的数据会被不断追加到该log 文件末端，且每条数据都有自己的 offset。消费者组中的每个消费者，都会实时记录自己消费到了哪个offset，以便出错恢复时，从上次的位置继续消费。

## 生产者的数据可靠性保证

| 方案                      | 优点                                             | 缺点                                                |
| ------------------------- | ------------------------------------------------ | --------------------------------------------------- |
| 半数以上完成同步就发送ack | 延迟低                                           | 选举新的leader时，容忍n台节点的故障，需要2n+1个副本 |
| 全部完成同步才发送ack     | 选举新的leader，容忍n台节点的故障，需要n+1个副本 | 延迟高                                              |

Kafka选择第二种方案，原因如下：

1. 同样为了容忍 n 台节点的故障，第一种方案需要 2n+1 个副本，而第二种方案只需要 n+1个副本，而Kafka 的每个分区都有大量的数据，第一种方案会造成大量数据的冗余。
2. 虽然第二种方案的网络延迟会比较高，但网络延迟对 Kafka 的影响较小。

### ISR

Leader 维护了一个动态的 in-sync replica set (ISR)，意为和 leader 保持同步的 follower 集合。当 ISR中的 follower 完成数据的同步之后，leader 就会给follower 发送 ack。

如果 follower长时间 未 向 leader 同 步 数 据 ， 则 该 follower 将 被 踢 出 ISR ， 该 时 间 阈 值 由replica.lag.time.max.ms 参数设定。Leader 发生故障之后，就会从 ISR 中选举新的 leader。

对于某些不太重要的数据，对数据的可靠性要求不是很高，能够容忍数据的少量丢失，所以没必要等ISR 中的 follower 全部接收成功。

所以 Kafka 为用户提供了三种可靠性级别，用户根据对可靠性和延迟的要求进行权衡。

- acks = 0：producer 不等待 broker 的 ack，这一操作提供了一个最低的延迟，broker一接收到还没有写入磁盘就已经返回，当 broker 故障时有可能丢失数据；
- acks = 1：producer 等待 broker 的 ack，partition 的 leader 落盘成功后返回 ack，如果在follower同步成功之前 leader 故障，那么将会丢失数据；
- acks = -1（all）：producer 等待 broker 的 ack，partition 的 leader 和 follower 全部落盘成功后才返回 ack。但是如果在 follower 同步完成后，broker 发送 ack 之前，leader 发生故障，那么会造成数据重复。

### HW&LEO

1. Producer向此Partition推送消息
2. Leader副本将消息追加到Log中，并递增其LEO
3. Follower副本从Leader副本拉取消息进行同步
4. Follower副本将拉取到的消息更新到本地Log中，并递增其LEO
5. 当ISR集合中所有副本都完成了对offset=11的消息的同步，Leader副本会递增HW

- LEO(Log End Offset)LEO是所有的副本都会有的一个offset标记，它指向追加到当前副本的最后一个消息 的offset。当生产者向Leader副本追加消息的时候，Leader副本的LEO标记会递增；当Follower副本成功从Leader副本拉取消息并更新到本地的时候，Follower副本的LEO就会增加。

- HWHW指的是消费者能见到的最大的 offset。

  HW标记了一个特殊的offset，当消费者处理 消息的时候，只能拉取到HW之前的消息，HW之后的消息对消费者来说是不可见的。与ISR集合类似，HW 也是由Leader副本管理的。当ISR集合中全部的Follower副本都拉取HW指定消息进行同步后，Leader副本会 递增HW的值。Kafka官方网站将HW之前的消息的状态称为“commit”，其含义是这些消息在多个副本中同时 存在，即使此时Leader副本损坏，也不会出现数据丢失。

### 故障处理细节

1. follower故障follower 发生故障后会被临时踢出 ISR，待该 follower 恢复后，follower 会读取本地磁盘记录的上次的 HW，并将 log 文件高于 HW 的部分截取掉，从 HW 开始向 leader 进行同步。等该 follower的 LEO 大于等于该 Partition 的 HW，即 follower 追上 leader 之后，就可以重新加入 ISR 了。

2. leader故障
   leader 发生故障之后，会从 ISR 中选出一个新的 leader，之后，为保证多个副本之间的数据一致性，其余的 follower 会先将各自的 log 文件高于 HW 的部分截掉，然后从新的leader同步数据。

   注意：这只能保证副本之间的数据一致性，并不能保证数据不丢失或者不重复。

### 如何处理消费过程中的重复消息

- at-most-once：保证数据不重复，但是不能保证数据不丢失。ACK=0

- at-least-once ：保证数据不丢失，但是不能保证数据不重复。ACK=-1

- exactly-once：数据既不重复也不丢失。
  exactly-once = At Least Once + 幂等性

- 幂等性
  所谓的幂等性就是指 Producer 不论向 Server 发送多少次重复数据，Server 端都只会持久化一条。
  要启用幂等性，只需要将 Producer 的参数中 enable.idompotence 设置为 true 即可。Kafka的幂等性实现其实就是将原来下游需要做的去重放在了数据上游。

  开启幂等性的 Producer 在初始化的时候会被分配一个 PID，发往同一 Partition 的消息会附带Sequence Number。 

  而Broker 端会对<PID, Partition, SeqNumber>做缓存，当具有相同主键的消息提交时，Broker 只会持久化一条。但是 PID 重启就会变化，同时不同的 Partition 也具有不同主键，所以幂等性无法保证跨分区跨会话的 Exactly Once。

## Kafka如何实现高性能IO

1. 消息批处理：减少网络通道开销。
2. 磁盘顺序写：减少寻道移臂开销。
   Kafka持久化消息到各个topic的partition文件时，是只追加的顺序写，充分利用了磁盘顺序访问快的特性，效率高。
3. 缓存页：减少磁盘IO开销。
   如果Kafka producer的生产速率与consumer的消费速率相差不大，那么就能几乎只靠对broker page cache的读写完成整个生产-消费过程，磁盘访问非常少。这个结论俗称为“读写空中接力”。
4. 零拷贝：减少数据多次拷贝的开销零拷贝是一种高效的数据传输机制，在追求低延迟的传输场景中十分常用。我们都知道，上下文切换是CPU密集型的工作，数据拷贝是I/O密集型的工作。零拷贝机制的终极目标，就是消除冗余的上下文切换和数据拷贝，提高效率。从内核空间到用户空间的来回复制是没有意义的，数据应该可以直接从内核缓冲区直接送入Socket缓冲区，消除了从内核空间到用户空间的来回复制，因此“zero-copy”这个词实际上是站在内核的角度来说的，并不是完全不会发生任何拷贝。