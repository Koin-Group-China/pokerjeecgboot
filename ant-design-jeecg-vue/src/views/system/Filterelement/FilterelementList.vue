<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="滤芯名称">
              <a-input placeholder="请输入滤芯名称" v-model="queryParam.filterelementName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="有效时长">
              <a-input placeholder="请输入有效时长" v-model="queryParam.validity"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('滤芯表')">导出</a-button>
     <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">
        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.images)" icon="user"/>
          </div>
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.filterelementId)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <filterelement-modal ref="modalForm" @ok="modalFormOk"></filterelement-modal>
  </a-card>
</template>

<script>
  import FilterelementModal from '../modules/FilterelementModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "FilterelementList",
    mixins:[JeecgListMixin],
    components: {
      FilterelementModal
    },
    data () {
      return {
        description: '滤芯表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   /*{
            title: '编号',
            align:"center",
            dataIndex: 'filterelementId'
           },*/
		   {
            title: '滤芯名称',
            align:"center",
            dataIndex: 'filterelementName'
           },
		   {
            title: '滤芯展示图',
            align:"center",
         dataIndex: 'images',
         scopedSlots: {customRender: "avatarslot"}
           },
		   {
            title: '有效时长',
            align:"center",
            dataIndex: 'validity'
           },
          {
            title: '滤芯最低更换天数',
            align: "center",
            dataIndex: 'replacementdays',
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/demo/filterelement/list",
          delete: "/demo/filterelement/delete",
          deleteBatch: "/demo/filterelement/deleteBatch",
          exportXlsUrl: "demo/filterelement/exportXls",
          importExcelUrl: "demo/filterelement/importExcel",
          imgerver: window._CONFIG['domianURL'] + "/sys/common/view",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
      getAvatarView: function (avatar) {
        return this.url.imgerver + "/" + avatar;
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>