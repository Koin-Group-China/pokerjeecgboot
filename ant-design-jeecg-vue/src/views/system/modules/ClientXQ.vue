<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户名">
          {{this.model.clientName}}
          <!--<a-input placeholder="请输入用户名" v-decorator="['clientName', validatorRules.clientName ]" />-->
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="编号">
          {{this.model.clientId}}
          <!--<a-input placeholder="请输入编号" v-decorator="['clientId', validatorRules.clientId ]" />-->
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="地址">
          {{this.model.address}}
          <!--<a-input placeholder="请输入地址" v-decorator="['address', validatorRules.address ]" />-->
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="手机号">
          {{this.model.phone}}
          <!--<a-input placeholder="请输入手机号" v-decorator="['phone', validatorRules.phone ]" />-->
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="开关设备编号">
          {{this.model.open}}
          <!--<a-input placeholder="请输入开关设备编号" v-decorator="['open', {}]" />-->
        </a-form-item>
        <!--<a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="密码">
          {{this.model.password}}
          <a-input placeholder="请输入密码" v-decorator="['password', validatorRules.password ]" />
        </a-form-item>-->


      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction } from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "ClientXQ",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
          address:{rules: [{ required: true, message: '请输入address!' }]},
          clientId:{rules: [{ required: true, message: '请输入clientId!' }]},
          clientName:{rules: [{ required: true, message: '请输入clientName!' }]},
          password:{rules: [{ required: true, message: '请输入password!' }]},
          phone:{rules: [{ required: true, message: '请输入phone!' }]},
        },
        url: {
          add: "/demo/client/add",
          edit: "/demo/client/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'address','clientId','clientName','open','password','phone'))
          //时间格式化
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        that.confirmLoading = false;
        that.close();
      },
      handleCancel () {
        this.close()
      },
    }
  }
</script>
<style lang="less" scoped>
</style>