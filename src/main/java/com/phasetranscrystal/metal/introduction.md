# 材料(Material)

材料用于描述一个物品的成分基本特征。

显然，一种材料需要具有在各方面的属性。这些特性通过`MaterialFeature`注册，比如`MetalMF`提供金属特性。  
一个特征应当继承`IMaterialFeature`接口，这一接口主要要求提供一组`Holder<MaterialItemType>`即特性所包含的类型物品，一组特性依赖。

让我们首先来明晰一下基本的元素：“熔点为400”的“铝合金”“块”在“氢气环境下”“熔化”，这其中的元素包含:

* 材料`Material`(`M`)：“铝合金”是这种材料的名字，材料本身与其获取方式，材质效果挂钩，一种材料将具有多种特性，决定这个材料可能的用途；
* 材料特性`MaterialFeature`(`MF`)：“熔点为400”是该材料的特征之一，也可以视为对于一种材料在某方面信息存储的基本单元。
  特性为材料在具体某方面的表现提供信息支持，换言之，材料在某种状态下的表现依赖于对应的特征；
* 材料物品类型`MaterialItemType`(`MIT`)：“块”是该材料当前表现出的状态。作为块，可能其不需要依赖于某种特征；
  但如果将其制作为导线，就需要依赖于电学相关的特征；制作为锯片，就需要依赖于硬度等特征。不过，对于各种形态，总有一点是要确定的：材料总量。
* 类型化材料对象`TypedMaterialObj`：“熔点为400的铝合金块”是最终在游戏中呈现出的一个对象，尽管实际上有关熔点的内容等通常不会直接被标注在名字中。
  类型化材料对象是材料系统数据架构与游戏可玩内容连接的主要方式，而大多数时候它们是自动生成的。通过对象内置的更多处理逻辑，在游戏中将材料的数据信息展现为不同的游戏内容。
* “氢气环境下”“熔化”等部分则基本可以归属为游戏内的内容了。通过拉取某物品对应材料的某些特征，可以便捷地让机器实现有关功能。

总结而言，整个材料系统的主体设计思想是**性质(MF)决定用途，结构(MIT)决定功能**。
其基本架构为：材料(Material) 具有多个 特性(MaterialFeature)，特性 为多个 类型(MaterialItemType)提供属性基础， 类型
最终在游戏中表现为不同用途。

这一系统的优势是：

1. 批量化的物品注册：类似格雷，匠魂这类强调一种材料不同形态的模组，手动地为每个物品进行注册，信息配置显然不太高效。
   本库中，一个材料包含的所有特性所拓展出的物品类型都将自动完成游戏内注册，以降低低价值的重复行为。
2. 标准化的程序分工：材料作为容器，特征提供信息，类型表现出形态，处理信息并直接或间接表现为游戏内容。
   明确的分层结构可以使得内容的拓展与信息的共用更加便捷与泛用，同时避免面向结果编程造成的内容批量修改困难。
3. 广泛的适用场景：即便minecraft原版，对于材料化的理念也有所体现，例如装备与工具的基本属性中，材料提供诸如耐久之类的数据，
   再根据对应要制成的物品修饰处理信息。这比人工填写每个数据显然更加便捷。

需要注意的是：部分用户可能会考虑，为什么特征的层级比类型高(M - MF - MIT结构)，
不应该是与游戏内容直接交叉的对象具有特征数据(M - MIT - MF)吗？

对于这一问题，想象一个场景：您在制作一个电学模组，电线的材料(20种)有不同的电阻，同时制成的方块有不同的粗细(8种)：

- 根据M - MIT - MF结构，您需要为不同材料制成的不同方块填入对应的数据，共计160组数据；
- 根据M - MF - MIT结构，您需要为不同材料配置电阻数据，并为8种方块添加独立或公用的算法，共计20组数据+8个算法或28组数据+1个算法。

M - MF - MIT结构的本质就是将对于原始数据的处理交给程序按照指定的算法进行处理计算，通过极少量的性能占用节省开发过程中填充数据的大量时间。
毕竟，除了敏感部分的性能需求，我猜大家都不想去把有计算公式的数据处理人工打表表示出来吧。（笑）

//TODO

您可以从[这里](MaterialItemType.java)开始通过javadoc阅读整个模块流程与实例。

# 运行流程

1. 数据收集阶段，这一阶段与mod信息收集阶段平行。在`RegisterEvent`
   事件，优先级为High的时候，所有被添加的[`Handle$Material`](Handler$Material.java)
   将被统一调用收集信息，同时dataGather标志将被设置为false。这意味着您可以在mod主类的构造或`RegisterEvent`
   的Highest优先级注册新的处理器。
2. 基本注册阶段，这一阶段将注册所有使用`DeferredRegistry`注册的材料，材料物品种类，材料特征。在这一阶段，材料与材料特征被调用时将会拉取
   阶段1 提供的，对于本对象的额外附加内容。在这之后，这两个表将会被清空。
3. 额外注册阶段，这一阶段与`RegisterEvent`事件，优先级为Low平行。在这一阶段中，所有上述内容已经完成注册，这时将会进行额外注册处理，为对应的材料与物品类型进行额外内容注册。
4. 注册结束阶段，这一阶段与优先级为Low的`RegistryEvent`
   事件平行。在这一阶段中，将会创建各种额外的映射表，比如`MF_CLASS2MFH(材料特征的class向材料特征处理器的映射表)`,`I2TMI(优先的物品获取材料信息的映射表)`,`M_MIT2I(优先的材料与物品类型获取对应物品的表)`
   等，在阶段1收集的其它信息也将在这一阶段被统一处理。在这之后infoBuild标志将被设置为false。

## [TypedMaterialItem材料类型物品](TypedMaterialItem.java) 与 [ITypedMaterialObj材料类型对象接口](ITypedMaterialObj.java)

材料类型物品是一个独立的物品，其内部包含的nbt数据将使其在不同材料条件下变成不同的样式。也就是说，所有这个形态的材料物品都是同一个物品。   
因此请不要直接通过物品判断来决定它是否为你需要的物品。这有可能导致错误，比如说，你需要下届合金齿轮，但匹配到一个地狱岩齿轮。

`TypedMaterialItem`在`Item`的基础上，主要提供了两个信息：

* 一个实例常量，存储`MaterialItemType`。
* 一个`Material`的nbt数据，存储物品的材料。

一个`TypedMaterialItem`应当实现`ITypedMaterialObj`接口。这一接口需要实现两个方法，分别是提供`MaterialItemType`
与`Material`。  
如果您不想为某个物品提供`MaterialItemType`但是想继续使用材料系统，您可以将前者提供一个Unexplained，并覆写`getContext`
方法来指定其物质含量。

## [MaterialItemType材料物品类型](MaterialItemType.java)

每种`MaterialItemType`将默认创建一个对应的`Item`。

材料物品类型中存在一个`createItem`方法，这个方法没有添加final修饰，意味着您可以覆写该内容。  
在通过Material与MaterialItemType创建物品时，程序将会先在Material中请求创建物品，若返回值为null，将会继续在MaterialItemType中请求，这时将不能返回null。  
这一设计用于处理一些诸如块之类的特殊物品。

## [IMaterialFeature材料特征](IMaterialFeature.java)

在新的设计中放弃了`MaterialFeatureHandle`，与其对应的核心功能将使用更为简洁`MaterialFeatureType`进行标记。这一更改主要为了便于材料特征的灵活处理。

对于`IMaterialFeature`提供的内容，主要是指定其对应的MaterialFeatureType，提供对应的物品类型等。需要实现的方法已经全部添加了javadoc，可以进入类下自行查看。

在`Material`中加入`MaterialFeature`，加载完成后会自动创建对应的表，方便进行属性特征查找。

## [Material材料](Material.java)

材料上面两种设计的最终使用者。在构造时需要传入对应的`MaterialFeature`以及id。其中`MaterialFeature`会自动在`Material`
类中创建表，可以使用需要的MFH来获取，物品所可用的物品形态类型同理。

材料中存在一个可能会需要被覆写的方法`createItem`，这一方法与`MaterialItemType`
中的功能相似。但是其可以返回null，表示回落给对应的`MaterialItemType`进行处理。

对于那些在游戏中不太常用的，作为中间产物的材料，您可以将其指定为`intermediateProduct`
，并为其选择颜色。这将直接使用纯色对材质基础进行叠加，并允许相同颜色的材质共用以降低占用。因此，请尽量使用少的颜色种类。

参考：银色使用`0xEBEEF0FF`。

## [Handler$Material材料额外处理器](Handler$Material.java)

材料额外处理器提供了一些特殊功能，如为其它mod的`MaterialFeature`添加`MaterialItemType`，为`Material`添加`MaterialFeature`
，以及为物品标记材料信息和添加材料信息到物品的获取。

这些内容的应当在mod主类构建时调用[材料系统总控](System$Material.java)
的`addMaterialExtraHandle(Consumer<Handler$Material> consumer)`方法实现。这些内容将会在优先级为high的`RegisterEvent`
时被调用，也就是绝大部分游戏内容注册之前。
