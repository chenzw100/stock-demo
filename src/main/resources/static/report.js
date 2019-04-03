var xData,x2Data,yData,y2Data,y3Data,y4Data,y5Data,y6Data,y7Data,y8Data,y9Data;
var myChart = echarts.init(document.getElementById('main'));
var myChart2 = echarts.init(document.getElementById('main2'));
var myChart3 = echarts.init(document.getElementById('main3'));
var myChart4 = echarts.init(document.getElementById('main4'));
var myChart5 = echarts.init(document.getElementById('main5'));
var myChart6 = echarts.init(document.getElementById('main6'));

$(function() {

    initView();

});
function initView(){
    getViewData();
    changeView();
}
function getSelect(){
    /*if(!checkSelectTime()){
        return false;
    }*/
    initView();
}
function changeView(){
    drawing();
}

function getViewData(){
        var end = $("#dateEnd").val();
        if(end==undefined || end==""){
            end="1";
        }
    $.ajax({
        url : 'index/'+end,
        dataType : 'json',
        type : 'get',
        async : false,
        success : function(ret){
                var d = ret;
                xData=d["x"];
            x2Data=d["x2"];

                yData=d["y"];
            y2Data=d["y2"];
            y3Data=d["y3"];

            y4Data=d["y4"];
            y5Data=d["y5"];
            y6Data=d["y6"];

            y7Data=d["y7"];
            y8Data=d["y8"];
            y9Data=d["y9"];
        }
    });
}
function drawing(){
    var option = {
        title : {
            text: '连板&涨停'
        },
        tooltip : {
            trigger: 'axis'
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xData
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                name:'连板',
                type:'line',
                smooth:true,
                data:yData,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            color : 'gray'
                        }
                    }
                },
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            },
            {
                name:'昨日',
                type:'line',
                data:y2Data,
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    var option2 = {
        title : {
            text: '市场温度'
        },
        tooltip : {
            trigger: 'axis'
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xData
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                name:'市场温度',
                type:'line',
                smooth:true,
                data:y3Data,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            color : 'gray'
                        }
                    }
                },
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            }
        ]
    };

    myChart2.setOption(option2);
    //=====
    var option3 = {
        title : {
            text: '开盘连板&涨停'
        },
        tooltip : {
            trigger: 'axis'
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xData
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                name:'连板',
                type:'line',
                smooth:true,
                data:y4Data,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            color : 'gray'
                        }
                    }
                },
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            },
            {
                name:'昨日',
                type:'line',
                data:y5Data,
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart3.setOption(option3);
    var option4 = {
        title : {
            text: '开盘市场温度'
        },
        tooltip : {
            trigger: 'axis'
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : xData
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                name:'市场温度',
                type:'line',
                smooth:true,
                data:y6Data,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            color : 'gray'
                        }
                    }
                },
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            }
        ]
    };

    myChart4.setOption(option4);
    //=====
    var option5 = {
        title : {
            text: '实时开盘连板&涨停'
        },
        tooltip : {
            trigger: 'axis'
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : x2Data
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                name:'连板',
                type:'line',
                smooth:true,
                data:y7Data,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            color : 'gray'
                        }
                    }
                },
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            },
            {
                name:'昨日',
                type:'line',
                data:y8Data,
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                }
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart5.setOption(option5);
    var option6 = {
        title : {
            text: '开盘市场温度'
        },
        tooltip : {
            trigger: 'axis'
        },

        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : x2Data
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                name:'实时市场温度',
                type:'line',
                smooth:true,
                data:y9Data,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            color : 'gray'
                        }
                    }
                },
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name : '平均值'}
                    ]
                }
            }
        ]
    };

    myChart6.setOption(option6);
}

function checkSelectTime(){
    if($('input:radio[name="item"]:checked').val() == "m") {
        if($("#d5").val()!=""){
            $("#startD").val($("#d5").val()+"-01");
        }else{
            $("#startD").val("");
        }
        if($("#d6").val()==""){
            $("#endD").val("");
        }

    }else{
        $("#startD").val($("#d1").val()),$("#endD").val($("#d2").val())
    }
    if($("#startD").val() == "" || $("#endD").val()=="" ){
        BootstrapDialog.show({
            title: '提示',
            message: '<div style="text-align: center;width:100%">请选择时间段!</div>',
            closable: false,
            size:BootstrapDialog.SIZE_SMALL,
            buttons: [{
                label: '确定',
                cssClass: 'btn-main2',
                action: function(dialogRef){
                    dialogRef.close();
                }
            }]
        }).getModalHeader().css('background-color', '#333');

        return false;
    }
    return true;
}







