import React from "react"
import { blog } from "../../dummydata"
import "./footer.css"

const Footer = () => (
  <>
    {/* <section className='newletter'>
                    <div className='container flexSB'>
                      <div className='left row'>
                        <h1>Newsletter - Stay tune and get the latest update</h1>
                        <span>Far far away, behind the word mountains</span>
                      </div>
                      <div className='right row'>
                        <input type='text' placeholder='Enter email address' />
                        <i className='fa fa-paper-plane'></i>
                      </div>
                    </div>
                  </section> */}
    <footer>
      <div className='container padding'>
        <div className='box logo'>
          <h1>APTECH</h1>
          <span>ONLINE EDUCATION & LEARNING</span>
          <p>BEST ONLINE STUDYING WEB.</p>

          <i className='fab fa-facebook-f icon'></i>
          <i className='fab fa-twitter icon'></i>
          <i className='fab fa-instagram icon'></i>
        </div>
        <div className='box link'>
          <h3>Our Social Media</h3>
          <ul>
            <li>facebook.com/aptech.fpt</li>
            {/* <li>youtube.com/@laptrinhvienquoctefptaptec7909/</li> */}
            <li>zalo.me/2731393687396192662</li>
            <li>tiktok.com/@fptaptechofficial</li>
            <li>https://aptech.fpt.edu.vn</li>
          </ul>
        </div>
        {/* <div className='box link'>
          <h3>Quick Links</h3>
          <ul>
            <li>Contact Us</li>
            <li>Pricing</li>
            <li>Terms & Conditions</li>
            <li>Privacy</li>
            <li>Feedbacks</li>
          </ul>
        </div> */}
        {/* <div className='box'>
            <h3>Recent Post</h3>
            {blog.slice(0, 3).map((val) => (
              <div className='items flexSB'>
                <div className='img'>
                  <img src={val.cover} alt='' />
                </div>
                <div className='text'>
                  <span>
                    <i className='fa fa-calendar-alt'></i>
                    <label htmlFor=''>{val.date}</label>
                  </span>
                  <span>
                    <i className='fa fa-user'></i>
                    <label htmlFor=''>{val.type}</label>
                  </span>
                  <h4>{val.title.slice(0, 40)}...</h4>
                </div>
              </div>
            ))}
        </div> */}
        <div className='box last'>
          <h3>Our Address</h3>
          <ul>
            <li>
              <i className='fa fa-map'></i>
              Số 8 Tôn Thất Thuyết, Phường Mỹ Đình, Quận Từ Liêm, Hà Nội
            </li>
            <li>
              <i className='fa fa-phone-alt'></i>
              590 Cách Mạng Tháng Tám, Phường 11, Quận 3, TP. HCM
            </li>
            <li>
              <i className='fa fa-paper-plane'></i>
              391A, Nam Kỳ Khởi Nghĩa, P. Võ Thị Sáu, Quận 3, TP. HCM
            </li>
            {/* <li>
          <i className='fa fa-paper-plane'></i>
          62 Đường số 07, KĐT Vạn Phúc, P. Hiệp Bình Phước, TP. Thủ Đức, TP. HCM
        </li> */}
          </ul>
        </div>
        <div class="col-md-6">
          <div id="map">
            <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3724.0964842999756!2d105.7797217750486!3d21.028825087776763!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3135ab86cece9ac1%3A0xa9bc04e04602dd85!2zRlBUIEFwdGVjaCBIw6AgTuG7mWkgLSBI4buHIFRo4buRbmcgxJDDoG8gVOG6oW8gTOG6rXAgVHLDrG5oIFZpw6puIFF14buRYyBU4bq_IChTaW5jZSAxOTk5KQ!5e0!3m2!1sen!2s!4v1691202275310!5m2!1sen!2s" width="200%" height="422px" frameborder="0"></iframe>
          </div>
        </div>
      </div>
    </footer>
    {/* <div className='legal'>
                    <p>
                      Copyright ©2022 All rights reserved | This template is made with <i className='fa fa-heart'></i> by GorkhCoder
                    </p>
                  </div> */}
  </>
)

export default Footer
