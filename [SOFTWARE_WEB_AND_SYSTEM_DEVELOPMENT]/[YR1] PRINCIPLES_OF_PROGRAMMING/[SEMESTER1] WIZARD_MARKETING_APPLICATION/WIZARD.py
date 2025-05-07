#Imported libraries and modules will be located here

import tkinter
from tkinter import messagebox, PhotoImage
import customtkinter
import bcrypt
import sqlite3
import qrcode
import cv2
import image



#------------------------------------------------------------------------------------------------------------------------
#Functions are placed here:
def login():
    e_address = e_address_textbox.get()
    password_login = password_textbox.get()

    if e_address == "" or password_login == "":
        messagebox.showerror("Login Error", "Please input missing details")

    else:
        add_to_database = sqlite3.connect("WIZARD.db")
        connect_to_database = add_to_database.cursor()
        connect_to_database.execute('''CREATE TABLE IF NOT EXISTS USERS (
                                         User_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                                         First_Name TEXT NOT NULL, 
                                         Last_Name TEXT NOT NULL, 
                                         Age INTEGER NOT NULL, 
                                         Email_Address TEXT NOT NULL, 
                                         Password TEXT NOT NULL)''')

        connect_to_database.execute('SELECT Password FROM USERS WHERE Email_Address=?', [e_address,])
        result = connect_to_database.fetchone()

        if result and bcrypt.checkpw(password_login.encode('utf-8'),result[0]):
            messagebox.showinfo("Login Successful", "You have successfully logged in")
            promotion_page()

        else:
            messagebox.showerror("Invalid Login Details", "Invalid Email Address or Password")

def to_signup():
    signup_window = customtkinter.CTkToplevel()
    signup_window.title("Signup To WizardSMA")
    signup_window.geometry("1000x550")
    signup_window.config(bg="white")
    signup_window.resizable(0, 0)

    global first,last,age,email_address,password_signup
    customtkinter.CTkLabel(master=signup_window, width=1000, height=100,text="WIZARD: Secure Marketing Application", text_color="white", font=font_1).pack(anchor=tkinter.CENTER)
    signup_frame = customtkinter.CTkFrame(master=signup_window, width=1000, height=825, corner_radius=10)
    signup_frame.pack(padx=100, pady=100)
    customtkinter.CTkLabel(master=signup_frame, text="SIGN UP", font=font_4).place(x=375, y=0.5)
    first = customtkinter.CTkEntry(master=signup_frame, placeholder_text="First Name", font=font_2, width=200, height=50, border_width=5, corner_radius=10)
    first.place(x=50, y=30)
    last = customtkinter.CTkEntry(master=signup_frame, placeholder_text="Last Name", font=font_2, width=200,height=50, border_width=5, corner_radius=10)
    last.place(x=300, y=30)
    age = customtkinter.CTkEntry(master=signup_frame, placeholder_text="Age", font=font_2, width=200, height=50,border_width=5, corner_radius=10)
    age.place(x=550, y=30)
    email_address = customtkinter.CTkEntry(master=signup_frame, placeholder_text="Email Address", font=font_2, width=200,height=50, border_width=5, corner_radius=10)
    email_address.place(x=175, y=120)
    password_signup = customtkinter.CTkEntry(master=signup_frame, placeholder_text="Password", font=font_2, width=200,height=50, border_width=5, corner_radius=10, show="*")
    password_signup.place(x=425, y=120)

    button = customtkinter.CTkButton(master=signup_frame, text="SIGNUP NOW", font=font_2, hover_color="grey", command=sign_up)
    button.place(relx=0.5, rely=0.85, anchor=tkinter.CENTER)


def sign_up():
        requirement_1 = "@gmail.com"
        add_to_database = sqlite3.connect("WIZARD.db")
        connected_database = add_to_database.cursor()
        connected_database.execute('''CREATE TABLE IF NOT EXISTS USERS (
                                 User_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                                 First_Name TEXT NOT NULL, 
                                 Last_Name TEXT NOT NULL, 
                                 Age INTEGER NOT NULL, 
                                 Email_Address TEXT NOT NULL, 
                                 Password TEXT NOT NULL)''')

        first_name = first.get()
        last_name = last.get()
        a = int(age.get())
        mail = email_address.get()
        en_code_password = password_signup.get()

        if first_name != "" and last_name != "" and a != "" and mail != "" and en_code_password != "" and a >= 18 and mail.__contains__(requirement_1) and len(en_code_password) > 10:
            hashed_password = bcrypt.hashpw(en_code_password.encode('utf-8'),bcrypt.gensalt())
            connected_database.execute('INSERT INTO USERS (First_Name, Last_Name, Age, Email_Address,Password) VALUES (?,?,?,?,?)', [first_name,last_name,a,mail,hashed_password])
            add_to_database.commit()
            messagebox.showinfo("Account Created", "WIZARD Account has been created")
            add_to_database.close()
        else:
            messagebox.showerror("Invalid Input","Please fill in all detail. Incorrect input has been given. Please input the correct input\n - Password must be more than 10 characters\n - Email must contain \"@gmail.com\"\n - Must be 18 years old or older")


def promotion_page():
    add_to_database = sqlite3.connect("WIZARD.db")
    connected_database = add_to_database.cursor()
    connected_database.execute('''CREATE TABLE IF NOT EXISTS PROMOTIONS (
                                     Promotion_ID INTEGER PRIMARY KEY NOT NULL UNIQUE,
                                     Promotion_Name TEXT NOT NULL UNIQUE, 
                                     Promotion_Brand TEXT NOT NULL, 
                                     Promotion_Discount_Multiplier REAL NOT NULL)''')

    add_to_database = sqlite3.connect("WIZARD.db")
    connected_database = add_to_database.cursor()
    connected_database.execute('''CREATE TABLE IF NOT EXISTS PROMOTION_INTERACTION (
                                         Promotion_Scanned INTEGER,
                                         Promotion_Generated INTEGER,
                                         Promotion_ID INTEGER,
                                         FOREIGN KEY (Promotion_ID) REFERENCES PROMOTIONS(Promotion_ID))''')


    promotions_window = customtkinter.CTkToplevel()
    promotions_window.title("WIZARD: Promotion Page")
    promotions_window.geometry("1000x550")
    promotions_window.config(bg="white")
    promotions_window.resizable(0,0)

    customtkinter.CTkLabel(master=promotions_window, width=1000, height=100, text=f"Welcome To The WIZARD Application",text_color="white", font=font_1).pack(anchor=tkinter.CENTER)
    promotion_frame = customtkinter.CTkScrollableFrame(master=promotions_window, width=1000, height=550)
    promotion_frame.pack()
    promotion_tab = customtkinter.CTkTabview(master=promotion_frame)
    promotion_tab.pack(padx=5, pady=5)
    promotion_tab.add("PROMOTIONS")
    promotion_tab.set("PROMOTIONS")
    promotion_tab.add("QR CODE SCANNER")

    p_frames = customtkinter.CTkFrame(master=promotion_tab.tab("PROMOTIONS"), width=1400, height=1000, corner_radius=0, fg_color="#80807f")
    p_frames.pack(padx=100, pady=100)


    qrscanner_frames = customtkinter.CTkFrame(master=promotion_tab.tab("QR CODE SCANNER"), width=500, height=300, corner_radius=0,fg_color="#80807f")
    qrscanner_frames.pack(padx=100, pady=100)


    global qrcode_entry
    qrcode_entry = customtkinter.CTkEntry(master=qrscanner_frames,
                                               placeholder_text="QRCode Filename",
                                               font=font_2,
                                               width=200,
                                               height=50,
                                               border_width=5,
                                               corner_radius=10)

    qrcode_entry.place(relx=0.5, rely=0.6, anchor=tkinter.CENTER)
    customtkinter.CTkButton(master=qrscanner_frames, text="Scan QR Code",font=font_2, hover_color="#050109",command=qr_code_read).place(relx=0.4, rely=0.8)

    promotion_one = customtkinter.CTkFrame(master=p_frames, width=1400, height=200, corner_radius=0, fg_color="#c54c3a",)
    promotion_one.pack(padx=0, pady=0)
    customtkinter.CTkButton(master=promotion_one, text="Generate QR CODE",font=font_2,hover_color="grey",command=promotion_one_qr).place(relx=0.41, rely=0.8)
    customtkinter.CTkLabel(master=promotion_one, text="PROMOTION 1", font=font_1,fg_color="#c54c3a",width=1400,height=200).pack(padx=100,pady=100)

    promotion_two = customtkinter.CTkFrame(master=p_frames, width=1400, height=200, corner_radius=0, fg_color="#5cb14e")
    promotion_two.pack(padx=1, pady=1)
    customtkinter.CTkButton(master=promotion_two, text="Generate QR CODE", font=font_2, hover_color="grey",command=promotion_two_qr).place(relx=0.41, rely=0.8)
    customtkinter.CTkLabel(master=promotion_two, text="PROMOTION 2", font=font_1, fg_color="#5cb14e", width=1400,height=200).pack(padx=100, pady=100)

    promotion_three = customtkinter.CTkFrame(master=p_frames, width=1400, height=200, corner_radius=0, fg_color="#c18c3e")
    promotion_three.pack(padx=0, pady=0)
    customtkinter.CTkButton(master=promotion_three, text="Generate QR CODE", font=font_2, hover_color="grey",command=promotion_three_qr).place(relx=0.41, rely=0.8)
    customtkinter.CTkLabel(master=promotion_three, text="PROMOTION 3", font=font_1, fg_color="#c18c3e", width=1400,height=200).pack(padx=100, pady=100)

    promotion_four = customtkinter.CTkFrame(master=p_frames, width=1400, height=200, corner_radius=0, fg_color="#f7fefe")
    promotion_four.pack(padx=0, pady=0)
    customtkinter.CTkButton(master=promotion_four, text="Generate QR CODE", font=font_2, hover_color="grey",command=promotion_four_qr).place(relx=0.41, rely=0.8)
    customtkinter.CTkLabel(master=promotion_four, text="PROMOTION 4", font=font_1, fg_color="#f7fefe", width=1400,height=200).pack(padx=100, pady=100)

def qr_code_read():
    file = qrcode_entry.get()
    img = cv2.imread(file)
    detect = cv2.QRCodeDetector()
    data, vert, binary = detect.detectAndDecode(img)

    if vert is not None:
        messagebox.showinfo("QRCode Scanned", "QRCode successfully scanned")
        print(data)
    else:
        messagebox.showerror("Error","Unable to read QRCode")


def promotion_one_qr():
    promotion_one_data_qr = "Detailed PROMOTION ONE information"
    promotion_one_file = "qr_code_files/promotion_one.png"
    generate_promotion_one_qrcode = qrcode.make(promotion_one_data_qr)
    generate_promotion_one_qrcode.save(promotion_one_file)
    messagebox.showinfo("QRCode Generated","QRCode has been generated")


def promotion_two_qr():
    promotion_two_data_qr = "Detailed PROMOTION TWO information"
    promotion_two_file = "qr_code_files/promotion_two.png"
    generate_promotion_two_qrcode = qrcode.make(promotion_two_data_qr)
    generate_promotion_two_qrcode.save(promotion_two_file)
    messagebox.showinfo("QRCode Generated", "QRCode has been generated")


def promotion_three_qr():
    promotion_one_data_qr = "Detailed PROMOTION THREE information"
    promotion_one_file = "qr_code_files/promotion_three.png"
    generate_promotion_one_qrcode = qrcode.make(promotion_one_data_qr)
    generate_promotion_one_qrcode.save(promotion_one_file)
    messagebox.showinfo("QRCode Generated", "QRCode has been generated")



def promotion_four_qr():
    promotion_one_data_qr = "Detailed PROMOTION FOUR information"
    promotion_one_file = "qr_code_files/promotion_four.png"
    generate_promotion_one_qrcode = qrcode.make(promotion_one_data_qr)
    generate_promotion_one_qrcode.save(promotion_one_file)
    messagebox.showinfo("QRCode Generated", "QRCode has been generated")




#------------------------------------------------------------------------------------------------------------------------
#Main Program
if __name__ == '__main__':


# -----------------------------------------------------------------------------------------------------------------------
#Customtkinter GUI Program

    customtkinter.set_appearance_mode("system")

    # Produces a desktop window of the login page.
    login_page = customtkinter.CTk()
    login_page.title("Login To WizardSMA")
    login_page.geometry("1000x550")
    login_page.config(bg="white")  # The background of the login page
    login_page.resizable(0, 0)

    font_1 = ("Georgia", 20, "bold")  # List of fonts used in the page
    font_2 = ("Georgia", 14)
    font_3 = ("Georgia", 11)
    font_4 = ("Georgia", 14, "underline")
        
#------------------------------------------------------------------------------------------------------------------------

    label_SMA = customtkinter.CTkLabel(master=login_page,
                                        width=1000,
                                        height=100,
                                        text="WIZARD: Secure Marketing Application",
                                        text_color="white",
                                        font=font_1)
    label_SMA.pack(anchor=tkinter.CENTER)

    login_frame = customtkinter.CTkFrame(master=login_page,  # Produces a frame in the middle
                                          width=700,
                                          height=825,
                                          corner_radius=10)
    login_frame.pack(padx=100, pady=100)

    e_address_textbox = customtkinter.CTkEntry(master=login_frame,
                                                placeholder_text="Email Address",
                                                font=font_2,
                                                width=200,
                                                height=50,
                                                border_width=5,
                                                corner_radius=10)
    e_address_textbox.place(relx=0.5, rely=0.2, anchor=tkinter.CENTER)

    password_textbox = customtkinter.CTkEntry(master=login_frame,
                                               placeholder_text="Password",
                                               font=font_2,
                                               width=200,
                                               height=50,
                                               border_width=5,
                                               corner_radius=10,
                                               show="*")
    password_textbox.place(relx=0.5, rely=0.5, anchor=tkinter.CENTER)

    login_button = customtkinter.CTkButton(master=login_frame,text="LOGIN", font=font_2, hover_color="grey", command=login).place(relx=0.5, rely=0.8, anchor=tkinter.CENTER)

    input_right_pass = customtkinter.CTkButton(master=login_frame,text="Sign Up For An Account", font=font_3, fg_color="black",hover_color="grey", command=to_signup).place(relx=0.5, rely=0.9, anchor=tkinter.CENTER)
#------------------------------------------------------------------------------------------------------------------------

    login_page.mainloop()