package com.ruoyi.project.party.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.party.domain.PartyOrgTreeData;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.party.mapper.DjPartyOrgMapper;
import com.ruoyi.project.party.domain.DjPartyOrg;
import com.ruoyi.project.party.service.IDjPartyOrgService;

/**
 * 党组织架构Service业务层处理
 *
 * @author ruoyi
 * @date 2020-08-03
 */
@Service
public class DjPartyOrgServiceImpl implements IDjPartyOrgService
{
    @Autowired
    private DjPartyOrgMapper djPartyOrgMapper;
    @Autowired
    private DjPartyMemberServiceImpl partyMemberService;


    @Override
    public List<Map<String,Object>> getOrgMemberChartData(){
        return djPartyOrgMapper.getOrgMemberChartData();
    }
    @Override
    public int getOrgCount(){
        return djPartyOrgMapper.getOrgCount();
    }
    /**
     * 查询党组织架构
     *
     * @param partyOrgId 党组织架构ID
     * @return 党组织架构
     */
    @Override
    public DjPartyOrg selectDjPartyOrgById(Long partyOrgId)
    {

        DjPartyOrg djPartyOrg = djPartyOrgMapper.selectDjPartyOrgById(partyOrgId);
        String[] djPartyOrgIds =djPartyOrg.getAncestors().split(",");
        String partyOrgFullName ="";
        for(String djPartyOrgId:djPartyOrgIds){
            if(!"0".equals(djPartyOrgId)){
                partyOrgFullName+=djPartyOrgMapper.selectDjPartyOrgById(Long.parseLong(djPartyOrgId)).getPartyOrgName()+"/";
            }
        }
        djPartyOrg.setPartyOrgFullName(partyOrgFullName+djPartyOrg.getPartyOrgName());
        if(StringUtils.isNotNull(djPartyOrg.getLeader())){
            djPartyOrg.setLeaderMember(partyMemberService.selectPartyMemberById(djPartyOrg.getLeader()));
        }
        return djPartyOrg;
    }

    /**
     * 查询党组织架构列表
     *
     * @param djPartyOrg 党组织架构
     * @return 党组织架构
     */
    @Override
    public List<DjPartyOrg> selectDjPartyOrgList(DjPartyOrg djPartyOrg)
    {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        List<DjPartyOrg> list =new ArrayList<DjPartyOrg>();
        if(SecurityUtils.isAdmin(sysUser.getUserId())){
            list = djPartyOrgMapper.selectDjPartyOrgList(djPartyOrg);
        }else{
            if(StringUtils.isNotNull(sysUser.getDjPartyMember())){
                list = selectPartyOrgLineByOrgId(sysUser.getDjPartyMember().getPartyOrgId());
            }
        }
        return list;
    }

    private List<DjPartyOrg> selectPartyOrgLineByOrgId(Long partyOrgId){
        List<DjPartyOrg> list =new ArrayList<DjPartyOrg>();
        list.addAll(djPartyOrgMapper.selectChildrenPartyOrgById(partyOrgId));
        list.addAll(selectParentPartyOrgById(partyOrgId));
        return list;
    }


    public List<DjPartyOrg>  selectParentPartyOrgById(Long partyOrgId)
    {
        List<DjPartyOrg> list =new ArrayList<DjPartyOrg>();
        DjPartyOrg org = djPartyOrgMapper.selectDjPartyOrgById(partyOrgId);
        list.add(org);
        Arrays.asList(org.getAncestors().split(",")).stream().forEach(parent->{
            if(!"0".equals(parent)){
                DjPartyOrg partyOrg = djPartyOrgMapper.selectDjPartyOrgById(Long.parseLong(parent));
                list.add(partyOrg);
            }
        });
        return list;

    }

    /**
     * 查询党组织架构下属列表
     *
     * @param partyOrgId 党组织架构Id
     * @return 党组织架构集合
     */
    @Override
    public List<DjPartyOrg> selectChildrenPartyOrgById(Long partyOrgId)
    {
        return djPartyOrgMapper.selectChildrenPartyOrgById(partyOrgId);
    }

    /**
     * 新增党组织架构
     *
     * @param djPartyOrg 党组织架构
     * @return 结果
     */
    @Override
    public int insertDjPartyOrg(DjPartyOrg djPartyOrg)
    {
        DjPartyOrg info = djPartyOrgMapper.selectDjPartyOrgById(djPartyOrg.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new CustomException("上级党组织架构停用，不允许新增");
        }
        djPartyOrg.setAncestors(info.getAncestors() + "," + djPartyOrg.getParentId());
        return djPartyOrgMapper.insertDjPartyOrg(djPartyOrg);
    }

    /**
     * 修改党组织架构
     *
     * @param djPartyOrg 党组织架构
     * @return 结果
     */
    @Override
    public int updateDjPartyOrg(DjPartyOrg djPartyOrg)
    {
        DjPartyOrg newParentPartyOrg = djPartyOrgMapper.selectDjPartyOrgById(djPartyOrg.getParentId());
        DjPartyOrg oldPartyOrg = djPartyOrgMapper.selectDjPartyOrgById(djPartyOrg.getPartyOrgId());
        if (StringUtils.isNotNull(newParentPartyOrg) && StringUtils.isNotNull(oldPartyOrg))
        {
            String newAncestors = newParentPartyOrg.getAncestors() + "," + newParentPartyOrg.getPartyOrgId();
            String oldAncestors = oldPartyOrg.getAncestors();
            djPartyOrg.setAncestors(newAncestors);
            updatePartyOrgChildren(djPartyOrg.getPartyOrgId(), newAncestors, oldAncestors);
        }
        int result = djPartyOrgMapper.updateDjPartyOrg(djPartyOrg);
        if (UserConstants.DEPT_NORMAL.equals(djPartyOrg.getStatus()))
        {
            // 如果该党组织架构是启用状态，则启用该党组织架构的所有上级部门
            updateParentPartyOrgStatus(djPartyOrg);
        }
        return result;
    }

    /**
     * 修改该党组织架构的上级党组织架构状态
     *
     * @param djPartyOrg 当前党组织架构
     */
    private void updateParentPartyOrgStatus(DjPartyOrg djPartyOrg)
    {
        String updateBy = djPartyOrg.getUpdateBy();
        djPartyOrg = djPartyOrgMapper.selectDjPartyOrgById(djPartyOrg.getPartyOrgId());
        djPartyOrg.setUpdateBy(updateBy);
        djPartyOrgMapper.updatePartyOrgStatus(djPartyOrg);
    }
    /**
     * 修改子元素关系
     *
     * @param partyOrgId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updatePartyOrgChildren(Long partyOrgId, String newAncestors, String oldAncestors)
    {
        List<DjPartyOrg> children = djPartyOrgMapper.selectChildrenPartyOrgById(partyOrgId);
        for (DjPartyOrg child : children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            djPartyOrgMapper.updatePartyOrgChildren(children);
        }
    }
    /**
     * 批量删除党组织架构
     *
     * @param partyOrgIds 需要删除的党组织架构ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyOrgByIds(Long[] partyOrgIds)
    {
        return djPartyOrgMapper.deleteDjPartyOrgByIds(partyOrgIds);
    }

    /**
     * 删除党组织架构信息
     *
     * @param partyOrgId 党组织架构ID
     * @return 结果
     */
    @Override
    public int deleteDjPartyOrgById(Long partyOrgId)
    {
        return djPartyOrgMapper.deleteDjPartyOrgById(partyOrgId);
    }

    /**
     * 校验党组织架构名称是否唯一
     *
     * @param djPartyOrg 党组织架构信息
     * @return 结果
     */
    @Override
    public String checkPartyOrgNameUnique(DjPartyOrg djPartyOrg)
    {
        Long partypOrgId = StringUtils.isNull(djPartyOrg.getPartyOrgId()) ? -1L : djPartyOrg.getPartyOrgId();
        DjPartyOrg info = djPartyOrgMapper.checkPartyOrgNameUnique(djPartyOrg.getPartyOrgName(), djPartyOrg.getPartyOrgId());
        if (StringUtils.isNotNull(info) && info.getPartyOrgId().longValue() != partypOrgId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据ID查询所有子党组织架构（正常状态）
     *
     * @param partyOrgId 党组织架构ID
     * @return 子党组织架构数
     */
    @Override
    public int selectNormalChildrenPartyOrgById(Long partyOrgId){
        return djPartyOrgMapper.selectNormalChildrenPartyOrgById(partyOrgId);
    }

    /**
     * 是否存在党组织架构子节点
     *
     * @param partyOrgId 党组织架构ID
     * @return 结果
     */
    @Override
    public boolean hasChildByPartyOrgId(Long partyOrgId){
        int result = djPartyOrgMapper.hasChildByPartyOrgId(partyOrgId);
        return result > 0 ? true : false;
    }

    /**
     * 党组织架构是否存在党员
     *
     * @param partyOrgId 党组织架构ID
     * @return 结果
     */
    public boolean checkOrgExistMember(Long partyOrgId){
        int result = djPartyOrgMapper.checkOrgExistMember(partyOrgId);
        return result > 0 ? true : false;
    }

    @Override
    public List<PartyOrgTreeData> getPartyOrgTreeDataList(){
        List<PartyOrgTreeData> list = null;
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        List<DjPartyOrg> partyOrgs =new ArrayList<DjPartyOrg>();
        if(!SecurityUtils.isAdmin(sysUser.getUserId())){
            if(StringUtils.isNotNull(sysUser.getDjPartyMember())){
                partyOrgs = selectPartyOrgLineByOrgId(sysUser.getDjPartyMember().getPartyOrgId());
                list = getfatherNode(djPartyOrgMapper.getPartyOrgTreeData(partyOrgs));
            }
        }else{
            list = getfatherNode(djPartyOrgMapper.getPartyOrgTreeData(null));
        }
        return list;
    }

    /**
     * 获取党组织架构树数据
     *
     * @return
     */
    @Override
    public PartyOrgTreeData getPartyOrgTreeData(){
        List<PartyOrgTreeData> list = getfatherNode(djPartyOrgMapper.getPartyOrgTreeData(null));
        if (StringUtils.isNotEmpty(list)){
            return list.get(0);
        }else{
            return null ;
        }
    }

    public List<PartyOrgTreeData> getfatherNode(List<PartyOrgTreeData> treeDataList) {
        List<PartyOrgTreeData> newTreeDataList = new ArrayList<PartyOrgTreeData>();
        for (PartyOrgTreeData jsonTreeData : treeDataList) {
            if(jsonTreeData.getParentId()==0) {
                //获取父节点下的子节点
                jsonTreeData.setChildren(getChildrenNode(jsonTreeData,treeDataList));
                //jsonTreeData.setState("open");
                newTreeDataList.add(jsonTreeData);
            }
        }
        return newTreeDataList;
    }

    private List<PartyOrgTreeData> getChildrenNode(PartyOrgTreeData fatherNode, List<PartyOrgTreeData> treeDataList) {
        List<PartyOrgTreeData> newTreeDataList = new ArrayList<PartyOrgTreeData>();
        for (PartyOrgTreeData jsonTreeData : treeDataList) {
            if(jsonTreeData.getParentId() == null){
                continue;
            }
            //这是一个子节点
            if(jsonTreeData.getParentId()==fatherNode.getId()){
                //递归获取子节点下的子节点
                jsonTreeData.setChildren(getChildrenNode(jsonTreeData , treeDataList));
                newTreeDataList.add(jsonTreeData);
            }
        }
        return newTreeDataList;
    }




}
