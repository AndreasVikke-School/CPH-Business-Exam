import React, { useState, useEffect } from 'react';
import facade from '../login/ApiFacade'
import { Alert } from 'react-bootstrap'
import WeekView from './WeekView'

export default function Calendar() {
  const [user, setUser] = useState({});
  const [recipes, setRecipes] = useState([]);
  const [week, setWeek] = useState({week: 1});
  const [messages, setMessages] = useState({});

  useEffect(() => {
    facade.fetchUser().then(res => setUser(res)).catch(e => console.log(e));
    facade.fetchAllRecipies().then(res => 
      setRecipes(res.sort(function(a, b) {
        if (a.id.toUpperCase() < b.id.toUpperCase()) return -1;
        if (a.id.toUpperCase() > b.id.toUpperCase()) return 1;
        return 0;
      }))).catch(e => console.log(e));
      if(user.userName !== undefined) {
        facade.fetchMenuPlanByWeek(1, user.userName).then(res => {
            let editWeek = {week: res.week};
            editWeek = {...editWeek, id: res.id};
            res.dayPlans.forEach(dayPlan => {
              editWeek[dayPlan.dayOfWeek] = dayPlan.recipeDTO.id;
            });
            setWeek(editWeek);
        });
      }
  }, [user.userName])

  const onChange = (evt) => {
    let key = evt.target.dataset.key;
    let value = evt.target.value;

    if(key === "week") {
      facade.fetchMenuPlanByWeek(value, user.userName).then(res => {
        let editWeek = {week: res.week};
        editWeek = {...editWeek, id: res.id};
        res.dayPlans.forEach(dayPlan => {
          editWeek[dayPlan.dayOfWeek] = dayPlan.recipeDTO.id;
        });
        setWeek(editWeek);
      }).catch(e => setWeek({ [key]: value}));
    } else {
      setWeek({ ...week, [key]: value});
    }
  }

  const saveWeek = () => {
    let emptyDays = [];
    for(let i = 1; i <= 7; i++)
      if(!Object.keys(week).includes(i.toString()))
        emptyDays.push(i);
    
    if(emptyDays.length > 0)
      setMessages({error: "Day(s) " + emptyDays.join(", ") + " has not been filled"});

    if(emptyDays.length === 0) {
      setMessages({});
      let dayPlans = [];
      for(let i = 1; i <= 7; i++)
        dayPlans.push({
          dayOfWeek: i,
          recipeDTO: {
            id: week[i].trim()
          }
        });

      let menuPlan = {
        week: week.week,
        user: {
          userName: user.userName
        },
        dayPlans: dayPlans
      };
      if(week.id === undefined) {
        facade.createMenuPlan(menuPlan).then(res => {setMessages({success: "Week saved succesfully"}); setWeek({...week, id: res.id})});
      } else {
        facade.editMenuPlan(menuPlan).then(res => {setMessages({success: "Week saved succesfully"});});
      }
    }
  }

  return (
    <div>
      <h3>Username: {user.userName}</h3>
      {messages.error !== undefined ? <Alert variant="danger">{messages.error}</Alert> : ""}
      {messages.success !== undefined ? <Alert variant="success">{messages.success}</Alert> : ""}
      <WeekView recipes={recipes} onChange={onChange} week={week} saveWeek={saveWeek} />
    </div>
  )
}