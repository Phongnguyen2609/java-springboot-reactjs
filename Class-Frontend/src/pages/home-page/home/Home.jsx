import React from "react"
import AboutCard from "../about/AboutCard"
import Hblog from "./Hblog"
import HAbout from "./HAbout"
import Hero from "./hero/Hero"
import Hprice from "./Hprice"
import Testimonal from "./testimonal/Testimonal"
import Header from "../common/header/Header"
import Footer from "../common/footer/Footer"

const Home = () => {
  return (
    <>
      <Header/>
      <Hero />
      <AboutCard />
      <HAbout />
      <Testimonal />
      <Hblog />
      <Hprice />
      <Footer/>
    </>
  )
}

export default Home
