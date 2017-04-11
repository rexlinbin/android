package com.bccv.tianji.model;

public class JoystickButtons {
	public static long     //没有任何按钮
    None = 0x0L,
    /// <summary>
    /// 上(左摇杆)
    /// </summary>
    L_UP = 0x01L,
    /// <summary>
    /// 下(左摇杆)
    /// </summary>
    L_Down = 0x02L,
    /// <summary>
    /// 左(左摇杆)
    /// </summary>
    L_Left = 0x04L,
    /// <summary>
    /// 右(左摇杆)
    /// </summary>
    L_Right = 0x08L,
    /// <summary>
    /// A
    /// </summary>
    A = 0x10L,
    /// <summary>
    /// B
    /// </summary>
    B = 0x20L,
    /// <summary>
    /// X
    /// </summary>
    X = 0x40L,
    /// <summary>
    /// Y
    /// </summary>
    Y = 0x80L,
    /// <summary>
    /// LB
    /// </summary>
    B5 = 0x100L,
    /// <summary>
    /// RB
    /// </summary>
    B6 = 0x200L,
    /// <summary>
    /// Back
    /// </summary>
    Back = 0x400L,
    /// <summary>
    /// Start
    /// </summary>
    Start = 0x800L,
    /// <summary>
    /// 左摇杆按下
    /// </summary>
    B9 = 0x1000L,
    /// <summary>
    /// 右摇杆按下
    /// </summary>
    B10 = 0x2000L,
    /// <summary>
    /// V1
    /// </summary>
    VolDown = 0x4000L,
    /// <summary>
    /// V2
    /// </summary>
    VolUp = 0x8000L,
    /// <summary>
    /// 上按键
    /// </summary>
    POV_UP = 0x10000L,
    /// <summary>
    /// 下按键
    /// </summary>
    POV_Down = 0x20000L,
    /// <summary>
    /// 左按键
    /// </summary>
    POV_Left = 0x40000L,
    /// <summary>
    /// 右按键
    /// </summary>
    POV_Right = 0x80000L,
    /// <summary>
    /// 左下 按键
    /// </summary>
    POV_LeftUp = 0x100000L,
    /// <summary>
    /// 右下 按键
    /// </summary>
    POV_RightUp = 0x200000L,
    /// <summary>
    /// 左上 按键
    /// </summary>
    POV_LeftDown = 0x400000L,
    /// <summary>
    /// 右下 按键
    /// </summary>
    POV_RightDown = 0x800000L,
    /// <summary>
    /// 上(右摇杆)
    /// </summary>
    R_UP = 0x01000000L,
    /// <summary>
    /// 下(右摇杆)
    /// </summary>
    R_Down = 0x02000000L,
    /// <summary>
    /// 左(右摇杆)
    /// </summary>
    R_Left = 0x04000000L,
    /// <summary>
    /// 右(右摇杆)
    /// </summary>
    R_Right = 0x08000000L,
    /// <summary>
    /// LT
    /// </summary>
    LT = 0x10000000L,
    /// <summary>
    /// RT
    /// </summary>
    RT = 0x20000000L,
    /// <summary>
    /// 主页键
    /// </summary>
	Home = 0x40000000L,
    /// <summary>
    /// 菜单键
    /// </summary>
	Menu = 0x80000000L;
}
