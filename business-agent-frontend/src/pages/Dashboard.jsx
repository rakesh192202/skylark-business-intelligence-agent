import { useState } from "react";
import axios from "axios";

import Sidebar from "../components/Sidebar";
import Header from "../components/Header";
import QuestionBox from "../components/QuestionBox";

function Dashboard(){

    const [question,setQuestion]=useState("");

    const [answer,setAnswer]=useState("");

    const [loading,setLoading]=useState(false);

    async function askAI(){

        if(!question.trim()) return;

        setLoading(true);

        try{

            const response=await axios.post(

                "https://skylark-business-intelligence-agent.onrender.com/api/chat",

                {

                    question

                }

            );

            setAnswer(response.data.answer);

        }

        catch(e){

            setAnswer("Error : "+e.message);

        }

        setLoading(false);

    }

    return(

        <div className="layout">

            <Sidebar setQuestion={setQuestion}/>

            <main className="content">

                <Header/>

                <QuestionBox

                    question={question}

                    setQuestion={setQuestion}

                    askAI={askAI}

                    loading={loading}

                />

                {

                    answer &&

                    <div className="responseCard">

                        <pre>

                            {answer}

                        </pre>

                    </div>

                }

            </main>

        </div>

    );

}

export default Dashboard;