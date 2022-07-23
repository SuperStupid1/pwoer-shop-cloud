<template>
  <div class="login">
    <div class="login-box">
      <div class="top">
        <div class="logo"><img src="~@/assets/img/login-logo.jpg"
                               alt=""></div>
      </div>
      <div class="mid">
        <el-form :model="dataForm"
                 :rules="dataRule"
                 ref="dataForm"
                 @keyup.enter.native="dataFormSubmit()"
                 status-icon>
          <el-form-item prop="userName">
            <el-input class="info"
                      v-model="dataForm.userName"
                      placeholder="帐号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input class="info"
                      v-model="dataForm.password"
                      type="password" Y
                      placeholder="密码"></el-input>
          </el-form-item>
<!--          <el-form-item prop="captcha">-->
<!--            <el-row :gutter="20">-->
<!--              <el-col :span="14">-->
<!--                <el-input v-model="dataForm.captcha"-->
<!--                          placeholder="验证码">-->
<!--                </el-input>-->
<!--              </el-col>-->
<!--              <el-col :span="10"-->
<!--                      class="login-captcha">-->
<!--                <img :src="captchaPath"-->
<!--                     @click="getCaptcha()"-->
<!--                     alt="">-->
<!--              </el-col>-->
<!--            </el-row>-->
<!--          </el-form-item>-->
          <el-form-item>
            <div class="item-btn"><input type="button"
                                         value="登录"
                                         @click="dataFormSubmit()"></div>
          </el-form-item>
        </el-form>
      </div>

      <div class="bottom">Copyright © 2019 北京动力节点信息科技有限公司</div>
    </div>
  </div>
</template>

<script>
  import {getUUID} from '@/utils'

  export default {
    data () {
      return {
        dataForm: {
          userName: '',
          password: '',
          uuid: '',
          captcha: ''
        },
        dataRule: {
          userName: [
            {required: true, message: '帐号不能为空', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '密码不能为空', trigger: 'blur'}
          ]
          // ,
          // captcha: [
          //   {required: true, message: '验证码不能为空', trigger: 'blur'}
          // ]
        },
        captchaPath: ''
      }
    },
    created () {
      // this.getCaptcha()
    },
    methods: {
      // 提交表单
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              // 拼接参数才行
              url: this.$http.adornUrl('/oauth/token?grant_type=password&username=' + this.dataForm.userName + '&password=' + this.dataForm.password),
              method: 'post',
              data: JSON.stringify({
                'grant_type': 'password',
                'username': this.dataForm.userName,
                'password': this.dataForm.password,
                'sessionUUID': this.dataForm.uuid,
                'imageCode': this.dataForm.captcha
              })
            }).then(({data}) => {
              // 登录成功了 token  存起来 方便请求的时候带上
              if (data.access_token) {
                //拿到token再存放cookie
                // this.$cookie.set('Authorization', 'bearer ' + data.access_token)
                localStorage.setItem('Authorization', 'bearer ' + data.access_token);
                // 路由到主页
                this.$router.replace({name: 'home'})
              } else {
                this.$router.replace({name: 'login'})
              }
            }).catch(() => {
              // this.getCaptcha()
            })
          }
        })
      },
      // 获取验证码
      getCaptcha () {
        this.dataForm.uuid = getUUID()
        this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${this.dataForm.uuid}`)
      }
    }
  }
</script>

<style lang="scss">
  .login {
    width: 100%;
    height: 100%;
    background: url(~@/assets/img/login-bg.png) no-repeat;
    background-size: cover;
    position: fixed;
  }

  .login .login-box {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    height: 100%;
    padding-top: 10%;
  }

  .login .login-box .top {
    margin-bottom: 30px;
    text-align: center;
  }

  .login .login-box .top .logo {
    font-size: 0;
  }

  .login .login-box .top .company {
    font-size: 16px;
    margin-top: 10px;
  }

  .login .login-box .mid {
    font-size: 14px;
  }

  .login .login-box .mid .item-btn {
    margin-top: 20px;
  }

  .login .login-box .mid .item-btn input {
    border: 0;
    width: 100%;
    height: 40px;
    box-shadow: 0;
    background: #1f87e8;
    color: #fff;
    border-radius: 3px;
  }

  .info {
    width: 410px;
  }

  .login-captcha {
    height: 40px;
  }

  .login .login-box .bottom {
    position: absolute;
    bottom: 10%;
    width: 100%;
    color: #999;
    font-size: 12px;
    text-align: center;
  }
</style>
