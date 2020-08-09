package com.ruoyi.project.dj.domain;

import lombok.Data;
import java.util.List;

@Data
public class PartyOrgTreeData {
    public Long id;       //json id 节点ID，对加载远程数据很重要。
    public Long parentId;      //
    public String label;     //json 显示节点文本。
    public List<PartyOrgTreeData> children;    //子节点
}
