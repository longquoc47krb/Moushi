import { ITheme } from "@/interfaces"


const redGradient: ITheme = {
    profileStyle: {
        background: 'linear-gradient(145deg, #e00000, #bc0000)',
    },
    iconStyle: {
        color: "#e00000"
    },
    messageStyle: {
        background: 'linear-gradient(145deg, #e00000, #bc0000)',
        color: "white"
    }

}
const witchingHour: ITheme = {
    profileStyle: {
        background: 'linear-gradient(45deg, #c31432, #240b36)',
    },
    iconStyle: {
        color: "#c31432"
    },
    messageStyle: {
        background: 'linear-gradient(45deg, #c31432, #240b36)',
        color: "white"
    }

}
const flare: ITheme = {
    profileStyle: {
        background: 'linear-gradient(45deg, #f12711, #f5af19)',
    },
    iconStyle: {
        color: "#f12711"
    },
    messageStyle: {
        background: 'linear-gradient(45deg, #f12711, #f5af19)',
        color: "white"
    }

}
const byDesign: ITheme = {
    profileStyle: {
        background: 'linear-gradient(45deg, #009FFF, #ec2F4B)',
    },
    iconStyle: {
        color: "#009FFF"
    },
    messageStyle: {
        background: 'linear-gradient(45deg, #009FFF, #ec2F4B)',
        color: "white"
    }

}
export const themes = {
    redGradient, witchingHour, flare, byDesign
}