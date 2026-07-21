function QuestionBox({

    question,
    setQuestion,
    askAI,
    loading

}) {

    const questions = [

        "How many open deals do we have?",

        "What is our total pipeline value?",

        "Which owner has the most deals?",

        "How many work orders are completed?",

        "Show business risks",

        "Give business summary"

    ];

    return (

        <>

            <div className="questionBox">

                <textarea

                    rows="3"

                    placeholder="Ask anything about your business..."

                    value={question}

                    onChange={(e)=>setQuestion(e.target.value)}

                />

                <button

                    onClick={askAI}

                    disabled={loading}

                >

                    {

                        loading ?

                        "Analyzing..."

                        :

                        "Ask AI"

                    }

                </button>

            </div>

            <div className="quickQuestions">

                {

                    questions.map((q,index)=>(

                        <button

                            key={index}

                            className="questionChip"

                            onClick={()=>setQuestion(q)}

                        >

                            {q}

                        </button>

                    ))

                }

            </div>

        </>

    );

}

export default QuestionBox;