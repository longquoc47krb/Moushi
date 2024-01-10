import { ITheme } from "@/interfaces"


const redGradient: ITheme = {
    iconStyle: {
        color: "#e00000"
    },
    backgroundStyle: {
        background: 'linear-gradient(145deg, #e00000, #bc0000)',
        color: "white"
    }

}
const witchingHour: ITheme = {
    iconStyle: {
        color: "#c31432"
    },
    backgroundStyle: {
        background: 'linear-gradient(45deg, #c31432, #240b36)',
        color: "white"
    }

}
const flare: ITheme = {
    iconStyle: {
        color: "#f12711"
    },
    backgroundStyle: {
        background: 'linear-gradient(45deg, #f12711, #f5af19)',
        color: "white"
    }

}
const byDesign: ITheme = {
    iconStyle: {
        color: "#009FFF"
    },
    backgroundStyle: {
        background: 'linear-gradient(45deg, #009FFF, #ec2F4B)',
        color: "white"
    }

}
const viciousStance: ITheme = {
    iconStyle: {
        color: "#29323c"
    },
    backgroundStyle: {
        backgroundImage: 'linear-gradient(60deg, #29323c 0%, #485563 100%)',
        color: "white"
    }
}
// background-image: linear-gradient(60deg, #29323c 0%, #485563 100%);
export const themes = {
    redGradient, witchingHour, flare, byDesign, viciousStance
}